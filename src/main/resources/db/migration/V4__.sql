ALTER TABLE api_access_kc_client
    ADD name VARCHAR(255) NOT NULL;

ALTER TABLE ipa_client
    ADD name VARCHAR(255) NOT NULL;

ALTER TABLE sync_kc_client
    ADD name VARCHAR(255) NOT NULL;

ALTER TABLE win_client
    ADD name VARCHAR(255) NOT NULL;
