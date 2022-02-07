
--guest info
select concat(g.first_name, ' ', g.last_name), g.email
from customer c
         join guest g on g.id_customer = c.id_customer_user
         join organized_event oe on oe.id_customer = c.id_customer_user

where oe.id_organized_event = 1;

--organizer info
select concat(c.first_name, ' ', c.last_name), u.email, c.phone_number
from customer c
         join organized_event oe on oe.id_customer = c.id_customer_user
         join users u on u.id_user = c.id_customer_user
where oe.id_organized_event = 1;

--event
select oe.name, oe.start_date, oe.end_date, et.type
from organized_event oe
         join event_type et on oe.id_event_type = et.id_event_type
where oe.id_organized_event = 1;

--location(s)
select lfe.time_from,
       lfe.time_to,
       l.name,
       concat(a.street_name, a.street_number, a.city, a.country, a.zip_code)
from organized_event oe
         join location_for_event lfe on oe.id_organized_event = lfe.id_organized_event
         join location l on lfe.id_location = l.id_location
         join address a on a.id_address = l.id_location_address
where oe.id_organized_event = 1;

--catering place
select ca.name, cfcl.date_time
from organized_event oe
         join location_for_event lfe on oe.id_organized_event = lfe.id_organized_event
         left join catering_for_chosen_location cfcl
                   on cfcl.id_catering_for_chosen_location = lfe.id_catering_for_chosen_location
         join catering ca on ca.id_catering = cfcl.id_catering
where oe.id_organized_event = 1;

--catering order
select ci.name, ci.type, coc.count
from organized_event oe
         join location_for_event lfe on oe.id_organized_event = lfe.id_organized_event
         join location l on lfe.id_location = l.id_location
         left join catering_for_chosen_location cfcl
                   on cfcl.id_catering_for_chosen_location = lfe.id_catering_for_chosen_location
         join catering_order_choice coc on cfcl.id_catering_for_chosen_location = coc.id_catering_for_chosen_location
         join catering_item ci on ci.id_catering_item = coc.id_catering_item
where oe.id_organized_event = 1;

--services and entertainment
select b.first_name, b.last_name, os.type, os.alias, sfe.time_from, sfe.time_to
from organized_event oe
         join location_for_event lfe on oe.id_organized_event = lfe.id_organized_event
         join service_for_event sfe on sfe.id_chosen_service_for_event = lfe.id_catering_for_chosen_location
         join optional_service os on sfe.id_optional_service = os.id_optional_service
         join business b on b.id_business_user = os.id_business
where oe.id_organized_event = 1;


