--
-- PostgreSQL database dump
--

\restrict PUHlX0r7iC1XJrRFg0OtYMqkZobbVnI7FR8ifoYrGgQVyfl5YHljslNDPRCiUid

-- Dumped from database version 17.6 (Debian 17.6-2.pgdg13+1)
-- Dumped by pg_dump version 17.6 (Debian 17.6-2.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY operaciones.tab_movimientos DROP CONSTRAINT IF EXISTS fkmxxun8x10pkedxtcionpul9g0;
ALTER TABLE IF EXISTS ONLY operaciones.tab_movimientos DROP CONSTRAINT IF EXISTS tab_movimientos_pkey;
ALTER TABLE IF EXISTS ONLY operaciones.tab_cuenta DROP CONSTRAINT IF EXISTS tab_cuenta_pkey;
ALTER TABLE IF EXISTS ONLY backoffice.tab_cliente DROP CONSTRAINT IF EXISTS tab_cliente_pkey;
DROP SEQUENCE IF EXISTS public.sec_movimiento;
DROP SEQUENCE IF EXISTS public.sec_cuenta;
DROP SEQUENCE IF EXISTS public.sec_cliente;
DROP TABLE IF EXISTS operaciones.tab_movimientos;
DROP TABLE IF EXISTS operaciones.tab_cuenta;
DROP TABLE IF EXISTS backoffice.tab_cliente;
DROP SCHEMA IF EXISTS operaciones;
DROP SCHEMA IF EXISTS backoffice;
--
-- Name: backoffice; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA backoffice;


--
-- Name: operaciones; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA operaciones;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: tab_cliente; Type: TABLE; Schema: backoffice; Owner: -
--

CREATE TABLE backoffice.tab_cliente (
    id_cliente bigint NOT NULL,
    fecha_actualizacion timestamp(6) without time zone,
    fecha_registro timestamp(6) without time zone,
    ip character varying(255),
    observacion character varying(255),
    direccion character varying(255),
    edad character varying(255),
    genero character varying(255),
    identificacion character varying(255),
    nombre character varying(255),
    telefono character varying(255),
    contrasena character varying(255),
    estado boolean,
    tipo_identificacion character varying(3),
    usuario character varying(255),
    CONSTRAINT tab_cliente_tipo_identificacion_check CHECK (((tipo_identificacion)::text = ANY ((ARRAY['CED'::character varying, 'RUC'::character varying])::text[])))
);


--
-- Name: tab_cuenta; Type: TABLE; Schema: operaciones; Owner: -
--

CREATE TABLE operaciones.tab_cuenta (
    id_cuenta bigint NOT NULL,
    fecha_actualizacion timestamp(6) without time zone,
    fecha_registro timestamp(6) without time zone,
    ip character varying(255),
    observacion character varying(255),
    cliente_id bigint NOT NULL,
    estado boolean,
    identificacion_cliente character varying(13),
    numero_cuenta character varying(255),
    saldo_disponible numeric(38,2),
    saldo_inicial numeric(38,2),
    tipo_cuenta character varying(3) NOT NULL,
    CONSTRAINT tab_cuenta_tipo_cuenta_check CHECK (((tipo_cuenta)::text = ANY ((ARRAY['AHO'::character varying, 'CTE'::character varying])::text[])))
);


--
-- Name: tab_movimientos; Type: TABLE; Schema: operaciones; Owner: -
--

CREATE TABLE operaciones.tab_movimientos (
    id_movimiento bigint NOT NULL,
    fecha_actualizacion timestamp(6) without time zone,
    fecha_registro timestamp(6) without time zone,
    ip character varying(255),
    observacion character varying(255),
    estado boolean,
    fecha_movimiento timestamp(6) without time zone,
    movimiento character varying(255),
    numero_cuenta character varying(255),
    saldo numeric(38,2),
    tipo_movimiento character varying(3) NOT NULL,
    valor_movimiento numeric(38,2),
    id_cuenta bigint,
    CONSTRAINT tab_movimientos_tipo_movimiento_check CHECK (((tipo_movimiento)::text = ANY ((ARRAY['DEP'::character varying, 'RET'::character varying])::text[])))
);


--
-- Name: sec_cliente; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sec_cliente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sec_cuenta; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sec_cuenta
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sec_movimiento; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sec_movimiento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Data for Name: tab_cliente; Type: TABLE DATA; Schema: backoffice; Owner: -
--

COPY backoffice.tab_cliente (id_cliente, fecha_actualizacion, fecha_registro, ip, observacion, direccion, edad, genero, identificacion, nombre, telefono, contrasena, estado, tipo_identificacion, usuario) FROM stdin;
19	2026-08-09 19:50:33.744	2026-08-09 19:50:24.827	127.0.1.1	ACTUALIZACIÓN DE DATOS	AV. ORELLANA 1452 ENTRE CALLE ALCIBAR Y CALLEJON 20	35	M	0945678903	PEDRO VELA	0999999999	123456	t	CED	0945678903
20	2026-08-09 23:04:51.902	2026-08-09 23:03:16.468	127.0.1.1	ACTUALIZACIÓN DE DATOS	9 DE OCTUBRE Y AV. QUITO			0956231478001	SERVICORP S.A.	04-124263	951234	t	RUC	0956231478001
\.


--
-- Data for Name: tab_cuenta; Type: TABLE DATA; Schema: operaciones; Owner: -
--

COPY operaciones.tab_cuenta (id_cuenta, fecha_actualizacion, fecha_registro, ip, observacion, cliente_id, estado, identificacion_cliente, numero_cuenta, saldo_disponible, saldo_inicial, tipo_cuenta) FROM stdin;
113	\N	2026-08-09 20:09:50.684	127.0.1.1	CREACION CUENTA	19	t	0945678903	8756557825	1752.75	100.00	CTE
133	\N	2026-08-09 23:05:13.203	127.0.1.1	CREACION CUENTA	20	t	0956231478001	5652356204	4301.04	100.00	CTE
108	2026-08-09 19:51:23.382	2026-08-09 19:50:47.99	127.0.1.1	CREACION CUENTA	19	t	0945678903	7753196476	1950.30	200.00	AHO
\.


--
-- Data for Name: tab_movimientos; Type: TABLE DATA; Schema: operaciones; Owner: -
--

COPY operaciones.tab_movimientos (id_movimiento, fecha_actualizacion, fecha_registro, ip, observacion, estado, fecha_movimiento, movimiento, numero_cuenta, saldo, tipo_movimiento, valor_movimiento, id_cuenta) FROM stdin;
71	\N	2026-08-09 19:52:57.794	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 19:52:57.794	Depósito valor 800.00	7753196476	870.00	DEP	800.00	108
72	\N	2026-08-09 19:53:07.256	127.0.1.1	CREACION RETIRO	t	2026-08-09 19:53:07.256	Retiro valor 100.00	7753196476	770.00	RET	100.00	108
73	\N	2026-08-09 19:53:11.493	127.0.1.1	CREACION RETIRO	t	2026-08-09 19:53:11.493	Retiro valor 10.00	7753196476	760.00	RET	10.00	108
74	\N	2026-08-09 19:53:18.053	127.0.1.1	CREACION RETIRO	t	2026-08-09 19:53:18.053	Retiro valor 300.00	7753196476	460.00	RET	300.00	108
75	\N	2026-08-09 19:53:27.637	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 19:53:27.636	Depósito valor 80.00	7753196476	540.00	DEP	80.00	108
76	\N	2026-08-09 19:53:32.945	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 19:53:32.945	Depósito valor 250.00	7753196476	790.00	DEP	250.00	108
77	\N	2026-08-09 19:54:42.536	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 19:54:42.536	Depósito valor 600.10	7753196476	1390.10	DEP	600.10	108
78	\N	2026-08-09 19:55:36.443	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 19:55:36.443	Depósito valor 600.10	7753196476	1990.20	DEP	600.10	108
79	\N	2026-08-09 19:55:39.144	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 19:55:39.144	Depósito valor 600.10	7753196476	2590.30	DEP	600.10	108
82	\N	2026-08-09 20:10:25.898	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 20:10:25.898	Depósito valor 600.10	8756557825	700.10	DEP	600.10	113
83	\N	2026-08-09 20:10:31.08	127.0.1.1	CREACION RETIRO	t	2026-08-09 20:10:31.08	Retiro valor 300.00	7753196476	2290.30	RET	300.00	108
86	\N	2026-08-09 20:20:44.999	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 20:20:44.999	Depósito valor 600.10	8756557825	1300.20	DEP	600.10	113
87	\N	2026-08-09 20:20:48.706	127.0.1.1	CREACION RETIRO	t	2026-08-09 20:20:48.706	Retiro valor 300.00	7753196476	1990.30	RET	300.00	108
88	\N	2026-08-09 20:21:04.045	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 20:21:04.045	Depósito valor 150.85	8756557825	1451.05	DEP	150.85	113
89	\N	2026-08-09 20:21:15.913	127.0.1.1	CREACION RETIRO	t	2026-08-09 20:21:15.913	Retiro valor 20.00	7753196476	1970.30	RET	20.00	108
93	\N	2026-08-09 20:29:42.878	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 20:29:42.878	Depósito valor 150.85	8756557825	1601.90	DEP	150.85	113
94	\N	2026-08-09 20:29:46.102	127.0.1.1	CREACION RETIRO	t	2026-08-09 20:29:46.102	Retiro valor 20.00	7753196476	1950.30	RET	20.00	108
100	\N	2026-08-09 21:03:51.633	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 21:03:51.633	Depósito valor 150.85	8756557825	1752.75	DEP	150.85	113
101	\N	2026-08-09 23:05:52.728	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 23:05:52.728	Depósito valor 5000.85	5652356204	5100.85	DEP	5000.85	133
102	\N	2026-08-09 23:06:14.602	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 23:06:14.602	Depósito valor 1000.106	5652356204	6100.96	DEP	1000.11	133
103	\N	2026-08-09 23:06:29.214	127.0.1.1	CREACION DEPÓSITO	t	2026-08-09 23:06:29.214	Depósito valor 200.08	5652356204	6301.04	DEP	200.08	133
104	\N	2026-08-09 23:06:45.757	127.0.1.1	CREACION RETIRO	t	2026-08-09 23:06:45.757	Retiro valor 500.00	5652356204	5801.04	RET	500.00	133
105	\N	2026-08-09 23:06:55.557	127.0.1.1	CREACION RETIRO	t	2026-08-09 23:06:55.557	Retiro valor 1500.00	5652356204	4301.04	RET	1500.00	133
\.


--
-- Name: sec_cliente; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.sec_cliente', 20, true);


--
-- Name: sec_cuenta; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.sec_cuenta', 133, true);


--
-- Name: sec_movimiento; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.sec_movimiento', 105, true);


--
-- Name: tab_cliente tab_cliente_pkey; Type: CONSTRAINT; Schema: backoffice; Owner: -
--

ALTER TABLE ONLY backoffice.tab_cliente
    ADD CONSTRAINT tab_cliente_pkey PRIMARY KEY (id_cliente);


--
-- Name: tab_cuenta tab_cuenta_pkey; Type: CONSTRAINT; Schema: operaciones; Owner: -
--

ALTER TABLE ONLY operaciones.tab_cuenta
    ADD CONSTRAINT tab_cuenta_pkey PRIMARY KEY (id_cuenta);


--
-- Name: tab_movimientos tab_movimientos_pkey; Type: CONSTRAINT; Schema: operaciones; Owner: -
--

ALTER TABLE ONLY operaciones.tab_movimientos
    ADD CONSTRAINT tab_movimientos_pkey PRIMARY KEY (id_movimiento);


--
-- Name: tab_movimientos fkmxxun8x10pkedxtcionpul9g0; Type: FK CONSTRAINT; Schema: operaciones; Owner: -
--

ALTER TABLE ONLY operaciones.tab_movimientos
    ADD CONSTRAINT fkmxxun8x10pkedxtcionpul9g0 FOREIGN KEY (id_cuenta) REFERENCES operaciones.tab_cuenta(id_cuenta);


--
-- PostgreSQL database dump complete
--

\unrestrict PUHlX0r7iC1XJrRFg0OtYMqkZobbVnI7FR8ifoYrGgQVyfl5YHljslNDPRCiUid

