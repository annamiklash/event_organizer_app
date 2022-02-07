package pjatk.pro.event_organizer_app.common.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import pjatk.pro.event_organizer_app.appproblem.model.dto.AppProblemDto;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvTools {

    private final static List<String> APP_PROBLEM_HEADERS =
            Arrays.asList("id", "title", "description", "createdAt", "resolvedAt");

    public String writeToFile(List<AppProblemDto> appProblems) {
        final String home = System.getProperty("user.home");
        final String fileName = "app_problem_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + ".csv";
        final String downloadPath = home + "/Downloads/" + fileName;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(downloadPath))) {
            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            csvPrinter.printRecord(APP_PROBLEM_HEADERS);
            appProblems.forEach(appProblem -> {
                try {
                    csvPrinter.printRecord(toRowValues(appProblem));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvPrinter.close(true);
            return downloadPath;
        } catch (IOException e) {
            throw new ActionNotAllowedException("Error during CSV export");
        }
    }

    private List<String> toRowValues(AppProblemDto appProblem) {
        final List<String> rowValues = new ArrayList<>();
        rowValues.add(Long.toString(appProblem.getId()));
        rowValues.add(appProblem.getConcern());
        rowValues.add(appProblem.getDescription());
        rowValues.add(appProblem.getCreatedAt());
        rowValues.add(appProblem.getResolvedAt());

        return rowValues;
    }
}
