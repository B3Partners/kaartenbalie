-- personal url string updaten naar alleen laatste stuk van code
-- http://digitree.kaartenbalie.nl:8181/kaartenbalie/services/099e00535c5511071b2c826b9a3b9100

-- create language
CREATE LANGUAGE plpgsql;

-- maken functie voor reverse string
create or replace function reverse_string(text) returns text as
'
DECLARE
reversed_string text;
incoming alias for $1;

BEGIN
reversed_string = '''';

for i in reverse char_length(incoming)..1 loop
reversed_string = reversed_string || substring(incoming from i for 1);
end loop;

return reversed_string;
END'
language plpgsql;

update users set personalurl = substring( personalurl from ( length(personalurl) - position('/' in reverse_string(personalurl)) + 1 ) + 1
for (length(personalurl)) ) where position('/' in personalurl) > 0;