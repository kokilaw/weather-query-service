CREATE TABLE api_key (
  api_key varchar(36) PRIMARY KEY,
  status varchar(36) NOT NULL,
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL
);

CREATE TABLE access_log (
  id bigint PRIMARY KEY,
  api_key varchar(36) NOT NULL,
  accessed_at timestamp without time zone NOT NULL,
  FOREIGN KEY (api_key) REFERENCES api_key (api_key)
);

create sequence access_log_seq increment by 50;

CREATE TABLE weather_update (
    id bigint PRIMARY KEY,
    city varchar(36) NOT NULL,
    country_code varchar(2) NOT NULL,
    period_code  varchar(10) NOT NULL,
    description  varchar(36) NOT NULL,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT weather_index UNIQUE (city, country_code, period_code)
);

create sequence weather_update_seq increment by 50;