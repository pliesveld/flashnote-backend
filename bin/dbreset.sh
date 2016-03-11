#!/bin/bash

SERVER=localhost
SERVER_ARG="-h ${SERVER}"
USER=postgres
USER_ARG="-U ${USER}"


if [[ ! -n POSTGRES_ENV ]]; then
POSTGRES_ENV
fi


PGPASSWORD=Pico psql ${SERVER_ARG} ${USER_ARG} <<EOF
DROP DATABASE learners;
CREATE DATABASE learners;
EOF

mvn antrun:run -Prefresh-db
