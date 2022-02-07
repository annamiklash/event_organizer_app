package pjatk.pro.event_organizer_app.user.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.business.model.Business;
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.common.util.EmailUtil;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.exceptions.InvalidCredentialsException;
import pjatk.pro.event_organizer_app.security.password.PasswordEncoderSecurity;
import pjatk.pro.event_organizer_app.user.mapper.UserMapper;
import pjatk.pro.event_organizer_app.user.model.User;
import pjatk.pro.event_organizer_app.user.model.dto.ChangePasswordDto;
import pjatk.pro.event_organizer_app.user.model.dto.LoginDto;
import pjatk.pro.event_organizer_app.user.model.dto.NewPasswordDto;
import pjatk.pro.event_organizer_app.user.model.dto.UserDto;
import pjatk.pro.event_organizer_app.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.exceptions.InvalidCredentialsException.Enum.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoderSecurity passwordEncoderSecurity;

    private final EmailService emailService;

    private final CustomerRepository customerRepository;

    private final BusinessRepository businessRepository;

    private final TimestampHelper timestampHelper;


    public ImmutableList<User> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<User> page = userRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(INCORRECT_CREDENTIALS));
    }

    public User get(long id) {
        final Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new InvalidCredentialsException(USER_NOT_EXISTS);
    }

    public UserDto getWithDetail(long id) {
        final User user = get(id);
        final Character type = user.getType();
        if (type == 'C') {
            final Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new InvalidCredentialsException(USER_NOT_EXISTS));
            return UserMapper.toDtoWithCustomer(user, customer);

        } else if (type == 'B') {
            final Business business = businessRepository.findById(id)
                    .orElseThrow(() -> new InvalidCredentialsException(USER_NOT_EXISTS));
            return UserMapper.toDtoWithBusiness(user, business);

        } else if (type == 'A') {
            return UserMapper.toDto(user);
        }
        throw new InvalidCredentialsException(USER_NOT_EXISTS);
    }

    @Transactional(rollbackOn = Exception.class)
    public void sendResetEmailLink(String email, String appUrl) {
        final User user = getUserByEmail(email);
        user.setResetPasswordToken(UUID.randomUUID().toString());
        userRepository.save(user);

        final String emailSubject = "Password Reset Request";
        final String content = "To reset your password, click the link below:\n" + appUrl
                + "/reset/token/" + user.getResetPasswordToken() + "\n\nSent via pro app";
        final SimpleMailMessage passwordResetEmail = EmailUtil.buildEmail(content, email, emailSubject, null);

        log.info("EMAIL: " + passwordResetEmail.toString());

        emailService.sendEmail(passwordResetEmail);
    }

    public void changePassword(long id, ChangePasswordDto dto) {
        final User user = get(id);
        final Boolean passwordsMatch = passwordEncoderSecurity.doPasswordsMatch(dto.getOldPassword(), user.getPassword());
        if (!passwordsMatch) {
            throw new InvalidCredentialsException(PASSWORDS_NOT_MATCH);
        }
        user.setPassword(passwordEncoderSecurity.bcryptEncryptor(dto.getNewPassword()));
        userRepository.save(user);
    }

    public void setNewPassword(String token, NewPasswordDto newPasswordDto) {
        final User user = getByResetPasswordToken(token);

        user.setPassword(passwordEncoderSecurity.bcryptEncryptor(newPasswordDto.getPassword()));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void block(long id) {
        final User user = get(id);
        user.setActive(false);
        user.setBlockedAt(timestampHelper.now());

        userRepository.save(user);
    }

    public void activate(long id) {
        final User user = get(id);
        user.setActive(true);
        user.setBlockedAt(null);
        user.setModifiedAt(timestampHelper.now());

        userRepository.save(user);
    }

    public boolean isActive(LoginDto loginDto) {
        return userRepository.active(loginDto.getEmail()).isPresent();
    }

    public Long count(String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        return userRepository.countAll(keyword);
    }

    private User getByResetPasswordToken(String token) {
        return userRepository.findUserByResetPasswordToken(token)
                .orElseThrow(() -> new InvalidCredentialsException(USER_NOT_EXISTS));
    }

}
