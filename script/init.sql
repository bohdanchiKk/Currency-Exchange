CREATE TABLE IF NOT EXISTS Currencies (
        id bigserial PRIMARY KEY,
        code varchar not null unique ,
        fullName varchar not null ,
        sign varchar not null
);

CREATE TABLE IF NOT EXISTS ExchangeRates (
        id bigserial PRIMARY KEY,
        baseCurrencyId bigint references Currencies(id),
        targetCurrencyId bigint references Currencies(id),
        rate decimal(10, 6)
        CHECK (baseCurrencyId <> targetCurrencyId)
);
INSERT INTO Currencies (code, fullName, sign) VALUES ('USD', 'United States Dollar', '$');
INSERT INTO Currencies (code, fullName, sign) VALUES ('EUR', 'Euro', '€');
INSERT INTO Currencies (code, fullName, sign) VALUES ('GBP', 'British Pound', '£');
INSERT INTO Currencies (code, fullName, sign) VALUES ('JPY', 'Japanese Yen', '¥');
INSERT INTO Currencies (code, fullName, sign) VALUES ('AUD', 'Australian Dollar', 'A$');
INSERT INTO Currencies (code, fullName, sign) VALUES ('CAD', 'Canadian Dollar', 'C$');
INSERT INTO Currencies (code, fullName, sign) VALUES ('CHF', 'Swiss Franc', 'Fr');

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (1, 2, 0.92); -- USD to EUR
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (1, 3, 0.78); -- USD to GBP
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (1, 4, 110.60); -- USD to JPY
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (1, 5, 0.91); -- USD to CHF
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (1, 6, 1.27); -- USD to CAD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (1, 7, 1.38); -- USD to AUD

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (2, 1, 1.09); -- EUR to USD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (2, 3, 0.85); -- EUR to GBP
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (2, 4, 120.50); -- EUR to JPY
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (2, 5, 0.99); -- EUR to CHF
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (2, 6, 1.38); -- EUR to CAD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (2, 7, 1.50); -- EUR to AUD

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (3, 1, 1.28); -- GBP to USD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (3, 2, 1.18); -- GBP to EUR
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (3, 4, 135.75); -- GBP to JPY
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (3, 5, 1.16); -- GBP to CHF
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (3, 6, 1.62); -- GBP to CAD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (3, 7, 1.75); -- GBP to AUD

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (4, 1, 0.009); -- JPY to USD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (4, 2, 0.0083); -- JPY to EUR
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (4, 3, 0.0074); -- JPY to GBP
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (4, 5, 0.0086); -- JPY to CHF
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (4, 6, 0.0119); -- JPY to CAD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (4, 7, 0.0125); -- JPY to AUD

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (5, 1, 1.10); -- CHF to USD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (5, 2, 1.01); -- CHF to EUR
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (5, 3, 0.86); -- CHF to GBP
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (5, 4, 116.35); -- CHF to JPY
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (5, 6, 1.39); -- CHF to CAD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (5, 7, 1.43); -- CHF to AUD

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (6, 1, 0.79); -- CAD to USD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (6, 2, 0.72); -- CAD to EUR
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (6, 3, 0.62); -- CAD to GBP
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (6, 4, 84.30); -- CAD to JPY
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (6, 5, 0.72); -- CAD to CHF
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (6, 7, 1.08); -- CAD to AUD

INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (7, 1, 0.72); -- AUD to USD
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (7, 2, 0.67); -- AUD to EUR
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (7, 3, 0.57); -- AUD to GBP
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (7, 4, 77.50); -- AUD to JPY
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (7, 5, 0.70); -- AUD to CHF
INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (7, 6, 0.93); -- AUD to CAD
