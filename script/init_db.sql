CREATE TABLE IF NOT EXISTS Currencies (
        id bigserial PRIMARY KEY,
        code varchar not null ,
        fullName varchar not null ,
        sign varchar not null
);

CREATE TABLE IF NOT EXISTS ExchngeRates (
        id bigserial PRIMARY KEY,
        baseCurrencyId bigint references Currencies(id),
        targetCurrencyId bigint references Currencies(id),
        rate decimal(6)
        CHECK (baseCurrencyId <> targetCurrencyId)
)