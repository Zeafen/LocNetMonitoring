CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE stub_users(
ID SMALLSERIAL PRIMARY KEY,
name CHARACTER VARYING(50) NOT NULL UNIQUE,
password TEXT NOT NULL,
role SMALLINT NOT NULL CHECK(role >= 0)
);

INSERT INTO stub_users(name, password, role) VALUES
('USER_ADMIN', '$2a$12$XiYeNQTyH3.HUtYCEMfsOOzNOgRDi7LAW3L7iAoGIgR27.b96b4uG', 0),
('USER_STUFF', '$2a$12$5EkWbIJcKPwrUJ4Ww6jAk.NTKmd1iWZrtE5tkX76jtg7E.8Fn5rT.', 2),
('USER_OPERATOR', '$2a$12$dFSaYLJoQvt6MZe/qiMZzuKvsui8k5AqRbJximYoT5TrEBxLc2fKu', 1);

CREATE TABLE machine_types(
	ID SMALLSERIAL PRIMARY KEY,
	name CHARACTER VARYING(25) NOT NULL UNIQUE
);
COMMENT ON TABLE machine_types IS 'Типы устройств';
COMMENT ON COLUMN machine_types.name IS 'Наименование типа устройства';

CREATE TABLE request_codes(
	code_number SMALLINT NOT NULL PRIMARY KEY CHECK (code_number > 0),
	succeed BOOLEAN NOT NULL,
	text CHARACTER VARYING(100),
	type_id INTEGER NOT NULL REFERENCES machine_types(ID) ON DELETE CASCADE
);
COMMENT ON TABLE request_codes IS 'Коды ответа типа устройства';
COMMENT ON COLUMN request_codes.code_number IS 'Номер кода ответа';
COMMENT ON COLUMN request_codes.succeed IS 'Успешность кода ответа';
COMMENT ON COLUMN request_codes.text IS 'Текст кода ответа';
COMMENT ON COLUMN request_codes.type_id IS 'Тип устройства';

CREATE TABLE machine_models(
	ID SMALLSERIAL PRIMARY KEY,
	name CHARACTER VARYING(25) NOT NULL,
	type_id SMALLINT NOT NULL REFERENCES machine_types(ID) ON DELETE CASCADE,
	years_expire SMALLINT NOT NULL CHECK(years_expire > 0),
	UNIQUE(type_id, name)
);
COMMENT ON TABLE machine_models IS 'Модели устройства';
COMMENT ON COLUMN machine_models.name IS 'Наименование модели';
COMMENT ON COLUMN machine_models.type_id IS 'Тип устройства модели';
COMMENT ON COLUMN machine_models.years_expire IS 'Количество лет годности модели';

CREATE TABLE model_standards(
	ID SERIAL PRIMARY KEY,
	parameter_name CHARACTER VARYING(50) NOT NULL CHECK(LENGTH(parameter_name) > 0),
	suggestion_value DECIMAL(12,4) NOT NULL CHECK (suggestion_value > 0),
	warning_value DECIMAL(12,4) NOT NULL CHECK (warning_value > 0 AND warning_value > suggestion_value),
	model_id SMALLINT REFERENCES machine_models(ID) NOT NULL,
	UNIQUE (model_id, parameter_name)
);
COMMENT ON TABLE model_standards IS 'Ограничения значений характеристик модели';
COMMENT ON COLUMN model_standards.parameter_name IS 'Наименование характеристики';
COMMENT ON COLUMN model_standards.suggestion_value IS 'Высокое значение характеристик'; 
COMMENT ON COLUMN model_standards.warning_value IS 'Критическое значение характеристики';

CREATE TABLE machines(
	ID INTEGER PRIMARY KEY,
	name CHARACTER VARYING(25) NOT NULL,
	address TEXT NOT NULL,
	serial_number INTEGER NOT NULL CHECK(serial_number > 0),
	date_produced TIMESTAMP WITH TIME ZONE NOT NULL,
	date_commissioning TIMESTAMP WITH TIME ZONE CHECK(date_commissioning IS NULL OR date_commissioning > date_produced),
	model_id SMALLINT NOT NULL REFERENCES machine_models(ID) ON DELETE CASCADE,
	UNIQUE(model_id, serial_number)
);
COMMENT ON TABLE machines IS 'Устройства';
COMMENT ON COLUMN machines.name IS 'Наименование устройства';
COMMENT ON COLUMN machines.address IS 'Адрес устройства (DNS)';
COMMENT ON COLUMN machines.serial_number IS 'Серийный номер устройства';
COMMENT ON COLUMN machines.date_produced IS 'Дата изготовления устройства';
COMMENT ON COLUMN machines.date_commissioning IS 'Дата принятия устройства на производство';
COMMENT ON COLUMN machines.model_id IS 'Модель устройства';


CREATE TABLE maintenance_types(
	ID SMALLSERIAL PRIMARY KEY,
	name CHARACTER VARYING(25) NOT NULL CHECK(LENGTH(name) > 0),
	period TEXT,
	UNIQUE(name, period)
);
COMMENT ON TABLE maintenance_types IS 'Виды технического обслуживания';
COMMENT ON COLUMN maintenance_types.name IS 'Наименование вида техниеского обслуживания';
COMMENT ON COLUMN maintenance_types.period IS 'Период проведения технического обслуживания';


CREATE TABLE maintenance(
	ID INTEGER PRIMARY KEY,
	work_description CHARACTER VARYING(255) NOT NULL,
	type_id SMALLINT REFERENCES maintenance_types(ID) ON DELETE CASCADE
);
COMMENT ON TABLE maintenance IS 'Технические обслуживания';
COMMENT ON COLUMN maintenance.work_description IS 'Описание работ технического обслуживания';
COMMENT ON COLUMN maintenance.type_id IS 'Вид технического обслуживания';


CREATE TABLE maintenance_records(
	ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	date_commissioned TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	date_finished TIMESTAMP WITH TIME ZONE,
	reason TEXT NOT NULL CHECK(LENGTH(reason) > 0),
	status SMALLINT NOT NULL DEFAULT 0 CHECK(status > 0),	
	maintenance_id INTEGER REFERENCES maintenance(ID) ON DELETE CASCADE,
	machine_id INTEGER REFERENCES machines(ID) ON DELETE CASCADE
);
COMMENT ON TABLE maintenance_records IS 'Записи технического обслуживания';
COMMENT ON COLUMN maintenance_records.date_commissioned IS 'Дата начала обслуживания';
COMMENT ON COLUMN maintenance_records.date_finished IS 'Дата окончания обслуживания';
COMMENT ON COLUMN maintenance_records.reason IS 'Причина отправки на Техническое обслуживание';
COMMENT ON COLUMN maintenance_records.status IS 'Статус технического обслуживания: 
0 - Обработка;
1 - Проверка;
2 - Проведение ТО;
3 - Завершено;
4 - Отклонено.';
COMMENT ON COLUMN maintenance_records.maintenance_id IS 'Проводимое ТО';
COMMENT ON COLUMN maintenance_records.machine_id IS 'Устройство, отправленное на ТО';

CREATE TABLE request_types(
	ID SMALLSERIAL PRIMARY KEY,
	name CHARACTER VARYING(25) UNIQUE
);
COMMENT ON TABLE request_types IS 'Типы запроса';
COMMENT ON COLUMN request_types.name IS 'Наименование Типа запроса';

CREATE TABLE content_types(
	ID SMALLSERIAL PRIMARY KEY,
	name CHARACTER VARYING(25) NOT NULL UNIQUE
);
COMMENT ON TABLE content_types IS 'Типы данных';
COMMENT ON COLUMN content_types.name IS 'Наименование типа данных';

CREATE TABLE journal_incoming(
	ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	session_id UUID NOT NULL DEFAULT uuid_generate_v4(),
	machine_id INTEGER NOT NULL REFERENCES machines(ID) ON DELETE CASCADE,
	request_type_id SMALLINT NOT NULL REFERENCES request_types(ID) ON DELETE CASCADE,
	request_code SMALLINT REFERENCES request_codes(code_number) ON DELETE CASCADE,
	request_data BYTEA,
	content_type_id SMALLINT REFERENCES content_types(ID) ON DELETE CASCADE CHECK(request_data IS NULL OR content_type_id IS NOT NULL),
	sent_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE journal_incoming IS 'Жунал входящих запросов на устройств';
COMMENT ON COLUMN journal_incoming.session_id IS 'Сессия запроса';
COMMENT ON COLUMN journal_incoming.machine_id IS 'Устройство, принявшее запрос';
COMMENT ON COLUMN journal_incoming.request_type_id IS 'Тип запроса';
COMMENT ON COLUMN journal_incoming.request_code IS 'Принятый код запроса';
COMMENT ON COLUMN journal_incoming.request_data IS 'Принятые Данные запроса';
COMMENT ON COLUMN journal_incoming.content_type_id IS 'Тип принятых данных';
COMMENT ON COLUMN journal_incoming.sent_time IS 'Дата принятия запроса';

CREATE TABLE journal_outcoming(
	ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	session_id UUID NOT NULL DEFAULT uuid_generate_v4(),
	machine_id INTEGER NOT NULL REFERENCES machines(ID) ON DELETE CASCADE,
	request_type_id SMALLINT NOT NULL REFERENCES request_types(ID) ON DELETE CASCADE,
	request_code SMALLINT REFERENCES request_codes(code_number) ON DELETE CASCADE,
	request_data BYTEA,
	content_type_id SMALLINT REFERENCES content_types(ID) ON DELETE CASCADE CHECK(request_data IS NULL OR content_type_id IS NOT NULL),
	sent_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE journal_outcoming IS 'Жунал исходящих запросов на устройств';
COMMENT ON COLUMN journal_outcoming.session_id IS 'Сессия запроса';
COMMENT ON COLUMN journal_outcoming.machine_id IS 'Устройство, отправившее запрос';
COMMENT ON COLUMN journal_outcoming.request_type_id IS 'Тип запроса';
COMMENT ON COLUMN journal_outcoming.request_code IS 'Отправленный код запроса';
COMMENT ON COLUMN journal_outcoming.request_data IS 'Отправленные Данные запроса';
COMMENT ON COLUMN journal_outcoming.content_type_id IS 'Типы отправленных ';
COMMENT ON COLUMN journal_outcoming.sent_time IS 'Дата отправки запроса';


CREATE  TABLE buffer(
	ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	machine_id INTEGER NOT NULL REFERENCES machines(ID) ON DELETE CASCADE,
	maintenance_id INTEGER REFERENCES maintenance(ID) ON DELETE CASCADE,
	is_read BOOLEAN NOT NULL DEFAULT FALSE,
	generated_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	buffer_type INTEGER NOT NULL CHECK(buffer_type >= 0),
	danger SMALLINT NOT NULL CHECK (danger >= 0) DEFAULT 0
);
COMMENT ON TABLE buffer IS 'Журнал оповещений системы';
COMMENT ON COLUMN buffer.machine_id IS 'Идентификатор устройства, для которого создано оповещение';
COMMENT ON COLUMN buffer.maintenance_id IS 'Идентификатор связанного ТО, для которого создано оповещение';
COMMENT ON COLUMN buffer.is_read IS 'Прочитано ли оповещение';
COMMENT ON COLUMN buffer.generated_date IS 'Дата генерации оповещения';
COMMENT ON COLUMN buffer.buffer_type IS 'Тип оповещения: 
0 - Изменение статистик устроцства;
1 - Гряждущие ТО;
2 - Изменение статуса ТО.';
COMMENT ON COLUMN buffer.danger IS 'Критичность сообщения: 
0 - Низкая опасность;
1 - Средняя опасность;
2 - Критисеский уровень уведомления.';


CREATE OR REPLACE FUNCTION get_device_succeed_percentage(machine_id INTEGER, outc BOOLEAN, selected_day TIMESTAMP WITH TIME ZONE)
RETURNS NUMERIC AS
$$
DECLARE total_count INTEGER;
	succeed_count INTEGER;
	table_name TEXT;
BEGIN
	IF outc = TRUE THEN
		table_name := 'public.journal_outcoming';
	ELSE
		table_name := 'public.journal_incoming';
	END IF;

	EXECUTE('SELECT COUNT(*)
FROM ' || table_name || ' AS j
INNER JOIN public.request_codes AS rc ON rc.code_number = j.request_code
WHERE date_trunc(''day'', j.sent_time) = CAST(''' || selected_day ||''' AS TIMESTAMP WITH TIME ZONE)
AND j.machine_id = ' || machine_id || ';') INTO total_count;

	IF total_count = 0 THEN
		total_count := 1;
	END IF;

	EXECUTE('SELECT COUNT(*)
FROM ' || table_name || ' AS j
INNER JOIN public.request_codes AS rc ON rc.code_number = j.request_code
WHERE rc.succeed = TRUE AND date_trunc(''day'', j.sent_time) = CAST(''' || selected_day ||''' AS TIMESTAMP WITH TIME ZONE)
AND j.machine_id = ' || machine_id || ';') INTO succeed_count;

	RETURN ROUND(succeed_count::numeric / total_count::numeric * 100);
END;
$$ LANGUAGE plpgsql;

--CALCULATE REQUESTS FREQUENCY PER MINUTE
CREATE OR REPLACE FUNCTION get_device_requests_frequency(machine_id INTEGER, outc BOOLEAN, selected_day TIMESTAMP WITH TIME ZONE)
RETURNS NUMERIC AS
$$
DECLARE average_count NUMERIC;
	table_name TEXT;
BEGIN
	IF outc = TRUE THEN
		table_name := 'public.journal_outcoming';
	ELSE
		table_name := 'public.journal_incoming';
	END IF;

	EXECUTE('SELECT AVG("sum") FROM (
	SELECT SUM("count") FROM (
		SELECT count(*), date_trunc(''minute'', sent_time) as "time" FROM ' || table_name || ' AS j
		WHERE date_trunc(''day'', j.sent_time) = CAST(''' || selected_day || ''' AS TIMESTAMP WITH TIME ZONE) AND j.machine_id = ' || machine_id || '
		GROUP BY "time") AS FUN1
	GROUP BY "time"
) AS FUN2;') INTO average_count;


	RETURN average_count;
END;
$$ LANGUAGE plpgsql;


--CALCULATE MAINTENANCE FREQUENCY PER MONTH
CREATE OR REPLACE FUNCTION get_device_maintenance_frequency(maintenance_id INTEGER, machine_id INTEGER)
RETURNS NUMERIC AS
$$
DECLARE average_count INTEGER;
BEGIN

	EXECUTE('SELECT AVG("sum") FROM (
	SELECT SUM("count") FROM (
		SELECT count(*), date_trunc(''month'', date_commissioned) as "time" FROM maintenance_records
		group by "time"
		union select count(*), date_trunc(''month'', date_finished) as "time" FROM maintenance_records
		group by "time") AS FUN1
	GROUP BY "time"
) AS FUN2;') INTO average_count;

	RETURN average_count;
END;
$$ LANGUAGE plpgsql;


--GENERATES RANDOM STRING
CREATE OR REPLACE FUNCTION random_str(str_length INTEGER)
RETURNS TEXT AS
$$
DECLARE random_string TEXT;
BEGIN

	IF str_length <= 0 THEN
		RAISE EXCEPTION 'Cannot generate string o length %', str_length;
	END IF;
	SELECT ARRAY_TO_STRING(
		ARRAY_AGG(
			chr(65 + 32 * CAST(ROUND(random()) AS INTEGER) + CAST(ROUND(random()*25) AS INTEGER))
		), ''
	)
	INTO random_string
	FROM generate_series(1, str_length);

	RETURN random_string;
END;
$$ LANGUAGE plpgsql;


--creating view to observe full journal record info
CREATE OR REPLACE VIEW journal_full_info AS 
SELECT j.ID as "ID", 
j.session_id, 
mt.name as "machine_type_name", 
mm.name AS "model_name", 
m.ID AS "machine_id",
m.name AS "machine_name", 
m.address, 
m.serial_number, 
FALSE AS "outcoming",  
rt.name AS "request_type", 
rc.code_number, 
rc.succeed, 
rc.text, 
j.request_data, 
ct.name AS "content_type", 
j.sent_time
FROM journal_incoming AS j
LEFT JOIN machines AS m ON m.ID = j.machine_id
LEFT JOIN machine_models AS mm ON m.model_id = mm.ID
LEFT JOIN machine_types AS mt ON mt.ID = mm.type_id
LEFT JOIN request_types AS rt ON rt.ID = j.request_type_id
LEFT JOIN content_types AS ct ON ct.ID = j.content_type_id
LEFT JOIN request_codes AS rc ON rc.code_number = j.request_code

UNION SELECT 
j.ID as "ID", 
j.session_id, 
mt.name as "machine_type_name", 
mm.name AS "model_name", 
m.ID AS "machine_id",
m.name AS "machine_name", 
m.address, 
m.serial_number, 
TRUE AS "outcoming",
rt.name AS "request_type", 
rc.code_number, 
rc.succeed, 
rc.text, 
j.request_data, 
ct.name AS "content_type", 
j.sent_time
FROM journal_outcoming AS j
LEFT JOIN machines AS m ON m.ID = j.machine_id
LEFT JOIN machine_models AS mm ON m.model_id = mm.ID
LEFT JOIN machine_types AS mt ON mt.ID = mm.type_id
LEFT JOIN request_types AS rt ON rt.ID = j.request_type_id
LEFT JOIN content_types AS ct ON ct.ID = j.content_type_id
LEFT JOIN request_codes AS rc ON rc.code_number = j.request_code;


--creating view to observe general information about machine
CREATE OR REPLACE VIEW machines_full_info AS 
SELECT m.ID as "ID", mt.name as "machine_type_name", mm.name AS "model_name", m.name AS "machine_name", m.address, m.serial_number, m.date_produced, m.date_commissioning
FROM machines AS m
LEFT JOIN machine_models AS mm ON m.model_id = mm.ID
LEFT JOIN machine_types AS mt ON mt.ID = mm.type_id;


--creating view to observe machine stats per day
CREATE MATERIALIZED VIEW device_stats AS
SELECT m.ID, FALSE as "outcoming",
date_trunc('day', j.sent_time) AS "sent_time", 
SUM(length(request_data)) AS "total_bytes_operated", 
ROUND(AVG(length(request_data))) AS "average_bytes_operated", 
COUNT(rc.code_number) AS "total_requests_count", 
get_device_succeed_percentage(m.ID, FALSE, date_trunc('day', j.sent_time)) AS "succeed_requests_percetage",
get_device_requests_frequency(m.ID, FALSE, date_trunc('day', j.sent_time)) AS "requests_frequency_rate"
FROM journal_incoming AS j
LEFT JOIN machines AS m ON m.ID = j.machine_id
LEFT JOIN request_codes AS rc ON rc.code_number = j.request_code
GROUP BY m.ID, "sent_time"

UNION SELECT m.ID, TRUE as "outcoming", 
date_trunc('day', j.sent_time) AS "sent_time", 
SUM(length(request_data)) AS "total_bytes_operated", 
ROUND(AVG(length(request_data))) AS "average_bytes_operated", 
COUNT(rc.code_number) AS "total_requests_count", 
get_device_succeed_percentage(m.ID, TRUE, date_trunc('day', j.sent_time)) AS "succeed_requests_percetage",
get_device_requests_frequency(m.ID, TRUE, date_trunc('day', j.sent_time)) AS "requests_frequency_rate"
FROM journal_outcoming AS j
LEFT JOIN machines AS m ON m.ID = j.machine_id
LEFT JOIN request_codes AS rc ON rc.code_number = j.request_code
GROUP BY m.ID, "sent_time";

--creating view to observe maintenance summary of the machine
CREATE OR REPLACE VIEW machine_maintenance_summary_view AS
SELECT	mt.ID AS "maintenance_id", mtr.machine_id AS "machine_id", mtt.ID AS "type_id",
mtt.name AS "type_name", 
MAX(mtr.date_finished) AS "last_maintenance_date", 
MAX(mtr.date_finished) + CAST(mtt.period AS INTERVAL) as "next_maintenance_date", 
mtt.period, 
mt.work_description,
get_device_maintenance_frequency(mt.ID, mtr.machine_id) AS "maintenance_frequency"
FROM maintenance_records AS mtr
INNER JOIN maintenance AS mt ON mt.ID = mtr.maintenance_id
INNER JOIN maintenance_types AS mtt ON mt.type_id = mtt.ID
GROUP BY mtr.machine_id, mt.ID, mtt.ID; 


--Creating sections of the journal by sent date and machine
CREATE OR REPLACE FUNCTION journal_initialize_machine_date_func()
	RETURNS TRIGGER AS
$$
DECLARE
	table_date TIMESTAMP WITH TIME ZONE;
	table_machine INTEGER;
	table_name TEXT;
	table_comment TEXT;
	column_name TEXT;
	current_request_code RECORD;
BEGIN
	IF TG_NARGS > 0 THEN
		column_name := TG_ARGS[0];
	ELSE
		column_name := 'sent_time';
	END IF;

	SELECT *
	INTO current_request_code
	FROM request_codes AS rc
	WHERE rc.code_number = NEW.request_code;

	IF(NOT EXISTS(
		SELECT 1 
		FROM machines AS m
		INNER JOIN machine_models AS mm ON mm.ID = m.model_id
		INNER JOIN machine_types AS mt ON mt.ID = mm.type_id
		WHERE m.ID = NEW.machine_id AND current_request_code.type_id = mt.ID)) THEN
		RAISE EXCEPTION 'Inappropriate request code (%) for machine %', NEW.request_code, NEW.machine_id;
	ELSIF(NOT EXISTS(
		SELECT 1
		FROM machines
		WHERE ID=NEW.machine_id AND date_commissioning < NEW.sent_time
	)) THEN
		RAISE EXCEPTION 'Cannot add request record of time earlier than the time related machine (%) has been commissioned', NEW.machine_id;
	END IF;

	EXECUTE('SELECT date_trunc(''day'', $1.' || column_name || ')') USING NEW INTO table_date;
	EXECUTE('SELECT $1.machine_id') USING NEW INTO table_machine;
	
	table_name := TG_TABLE_NAME || '_' || table_machine || '_' || to_char(table_date, 'YYYY_MM_DD');
	table_comment := (SELECT obj_description((TG_TABLE_SCHEMA || '.' || TG_TABLE_NAME)::regclass) || ' за ' || to_char(table_date, 'YYYY_MM_DD') || ' для устройства ' || table_machine);
	
	IF (NOT EXISTS (SELECT 1 FROM pg_tables WHERE tablename=table_name AND schemaname=TG_TABLE_SCHEMA)) THEN
		EXECUTE(
		'CREATE TABLE IF NOT EXISTS ' || TG_TABLE_SCHEMA || '.' || table_name || ' (
			CONSTRAINT '||TG_TABLE_SCHEMA || '_' || table_name || '_' || column_name || '_check' || ' CHECK(date_trunc(''day'', ' || column_name || ')=''' || table_date || '''),
			CONSTRAINT '||TG_TABLE_SCHEMA || '_' || table_name || '_machine_check' || ' CHECK(machine_id=' || table_machine || ')
		)
		INHERITS (' || TG_TABLE_SCHEMA || '.' || TG_TABLE_NAME || ')
		WITH(OIDS=FALSE);

		CREATE INDEX ' || TG_TABLE_SCHEMA || '_' || table_name || '_index ON ' || TG_TABLE_SCHEMA || '.' || table_name || ' (' || column_name || ');
		CREATE INDEX ' || TG_TABLE_SCHEMA || '_' || table_name || '_machine_index ON ' || TG_TABLE_SCHEMA || '.' || table_name || ' (machine_id);

		GRANT INSERT ON ' || table_name ||' TO journal_logger_grp;');
	

		EXECUTE FORMAT('COMMENT ON TABLE ' || TG_TABLE_SCHEMA || '.' || table_name || ' IS  ''' || table_comment || ''' ;');
	END IF;
	
	EXECUTE('INSERT INTO ' || TG_TABLE_SCHEMA || '.' || table_name || ' VALUES ($1.*);') USING NEW;
	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION journal_initialize_machine_date_func()
	OWNER TO postgres;

CREATE OR REPLACE TRIGGER journal_initialize_date_trg
BEFORE INSERT ON journal_incoming
FOR EACH ROW
EXECUTE PROCEDURE journal_initialize_machine_date_func();

CREATE OR REPLACE TRIGGER journal_initialize_date_trg
BEFORE INSERT ON journal_outcoming
FOR EACH ROW
EXECUTE PROCEDURE journal_initialize_machine_date_func();


CREATE OR REPLACE FUNCTION maintenance_records_status_change_func()
	RETURNS TRIGGER AS
$$
BEGIN
	INSERT INTO buffer(machine_id, maintenance_id, buffer_type, danger)
	VALUES(NEW.machine_id, NEW.maintenance_id, 2, 0);

	IF(TG_OP = 'UPDATE') THEN
		CASE
			WHEN OLD.status <> 2  AND NEW.status = 3 THEN 
				RAISE EXCEPTION 'Невозможно обновить статус записи ТО % до ''Завершено'' не со статуса ''Проведение ТО''', OLD.id;

			WHEN OLD.status = 3 AND NEW.status <> 0 AND NEW.status <> 1 OR NEW.status = 3 AND OLD.status <> 0 AND OLD.status <> 1 THEN 
				RAISE EXCEPTION 'Обновить статус записи ТО % до ''Завершено'' можно лишь со статуса ''Проверка'' и ''Обработка''. Так же как и восстановить состояние можно только указанных статусов.', OLD.id;

			WHEN OLD.status = 4 AND NEW.status <> 0 AND NEW.status <> 1 OR NEW.status = 4 AND OLD.status <> 0 AND OLD.status <> 1 THEN 
				RAISE EXCEPTION 'Обновить статус записи ТО % до ''Отклонено'' можно лишь со статуса ''Проверка'' и ''Обработка''. Так же как и восстановить состояние можно только указанных статусов.', OLD.id;
		END CASE;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER maintenance_records_status_change_trg
AFTER UPDATE OF status
ON maintenance_records
FOR EACH ROW
EXECUTE PROCEDURE maintenance_records_status_change_func();

CREATE OR REPLACE TRIGGER maintenance_records_status_change_trg
AFTER INSERT ON maintenance_records
FOR EACH ROW
EXECUTE PROCEDURE maintenance_records_status_change_func();

CREATE OR REPLACE FUNCTION update_device_stats()
RETURNS VOID AS
$$
DECLARE stats_record JSONB;
	is_info BOOLEAN;
	is_average BOOLEAN;
BEGIN 

	EXECUTE(
		'REFRESH MATERIALIZED VIEW public.device_stats;'
	);

	for stats_record in SELECT CAST(row_to_json(ds) AS JSONB) 
		from device_stats as ds
		where sent_time = date_trunc('day', current_timestamp)
	loop
		IF NOT EXISTS(
				SELECT 1 FROM model_standards AS ms
				INNER JOIN machines as m ON m.id = CAST(stats_record->'id' AS INTEGER)
				WHERE ms.model_id = m.model_id
			) THEN
			CONTINUE;
		END IF;
	
		SELECT EXISTS(
			SELECT * FROM jsonb_each(stats_record) AS param
			INNER JOIN machines as m ON m.id = CAST(stats_record->'id' AS INTEGER)
			WHERE EXISTS(
				SELECT 1 FROM model_standards AS ms
				WHERE ms.parameter_name = param.key 
				AND ms.model_id = m.model_id
				AND (CAST(ms.suggestion_value AS NUMERIC) >= CAST(param.value AS NUMERIC) AND ms.greater = False OR
				CAST(ms.suggestion_value AS NUMERIC) <= CAST(param.value AS NUMERIC) AND ms.greater = True)
			)
		) INTO is_info;
		
		IF is_info = TRUE THEN
		 RETURN;
		ELSE
			IF(EXISTS(
				SELECT * FROM jsonb_each(stats_record) AS param
				INNER JOIN machines as m ON m.id = CAST(stats_record->'id' AS INTEGER)
				WHERE EXISTS(
					SELECT 1 FROM model_standards AS ms
					WHERE ms.parameter_name = param.key 
					AND ms.model_id = m.model_id
					AND (CAST(ms.warning_value AS NUMERIC) >= CAST(param.value AS NUMERIC) AND ms.greater = False OR
						CAST(ms.warning_value AS NUMERIC) <= CAST(param.value AS NUMERIC) AND ms.greater = True)
			))) THEN
				INSERT INTO buffer(machine_id, buffer_type, danger)
				VALUES(CAST(stats_record->'id' AS INTEGER), 0, 2);
			ELSE
				INSERT INTO buffer(machine_id, buffer_type, danger)
				VALUES(CAST(stats_record->'id' AS INTEGER), 0, 1);
			END IF;
		END IF;
		
	end loop;
	
END;
$$
LANGUAGE plpgsql;