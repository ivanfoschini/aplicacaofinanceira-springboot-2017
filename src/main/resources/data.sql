INSERT INTO public.account(account_id, username, password, enabled, credentials_expired, expired, locked)
    VALUES (nextval('account_sequence'), 'admin', '$2a$06$q6gJAAuDELTvMTH9/aWvP.GpGAVAlAc725Hdbbz/ha6wTUK3t20H6', true, false, false, false);

INSERT INTO public.account(account_id, username, password, enabled, credentials_expired, expired, locked)
    VALUES (nextval('account_sequence'), 'funcionario', '$2a$06$eIW6EOPlc0B8djt/LOQT0ewAztx.qIUQHzutuYT9F.cKp8PPjpbHC', true, false, false, false);

INSERT INTO public.role(role_id, code, label) VALUES (nextval('role_sequence'), 'ROLE_ADMIN', 'ADMIN');
INSERT INTO public.role(role_id, code, label) VALUES (nextval('role_sequence'), 'ROLE_FUNCIONARIO', 'FUNCIONARIO');

INSERT INTO public.account_role(account_id, role_id) VALUES (currval('account_sequence') - 1, currval('role_sequence') - 1);
INSERT INTO public.account_role(account_id, role_id) VALUES (currval('account_sequence'), currval('role_sequence'));