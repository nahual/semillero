CREATE USER 'semillero' IDENTIFIED BY 'semillero';

CREATE DATABASE semillero DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

USE semillero;

GRANT ALL ON semillero.* TO semillero;