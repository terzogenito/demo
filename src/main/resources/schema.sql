CREATE TABLE memberships (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    profile_image VARCHAR(255)
);

CREATE TABLE services (
    service_code VARCHAR(50) PRIMARY KEY,
    service_tariff NUMERIC,
    transaction_type VARCHAR(20),
    service_name VARCHAR(255),
    service_icon VARCHAR(255),
    description TEXT
);

CREATE TABLE transactions (
    invoice_number VARCHAR(50) PRIMARY KEY,
    user_id INT,
    service_code VARCHAR(50),
    total_amount NUMERIC,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE banners (
    id SERIAL PRIMARY KEY,
    banner_name VARCHAR(255) NOT NULL,
    banner_image VARCHAR(255) NOT NULL,
    description TEXT
);
