CREATE TABLE returnorder
(
  id VARCHAR(15) PRIMARY KEY NOT NULL,
  cust_name VARCHAR(20) NOT NULL,
  cust_contact_details VARCHAR(15) NOT NULL,
  comp_type VARCHAR(20) NOT NULL,
  comp_name VARCHAR(100) NOT NULL,
  details VARCHAR(256) NOT NULL,
  qty INT NOT NULL,
  process_charge DOUBLE PRECISION NOT NULL,
  pack_and_del_charge DOUBLE PRECISION NOT NULL,
  del_date VARCHAR(70) NOT NULL,
  created_at VARCHAR(70) NOT NULL
);