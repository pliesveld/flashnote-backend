#!/bin/bash

POSTGRES_ENV
PGPASSWORD=pico psql -U postgres <<EOF
DROP DATABASE learners;
CREATE DATABASE learners;
EOF
