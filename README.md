# Summary

Tool to generate monitoring and documentation of a Project (defined in an Excel file) and its resources, usefull in datacenter operations.

Angel Galindo Muñoz ( zoquero@gmail.com ), November of 2018

# Features

It validates the Excel file.

It generates:
* script to generate monitoring (based on a "addHost" and "addService" API)
* mediawiki documents with:
** inventory
** procedures for incidences
** procedures for requests
* periodic operations
* lifecycle operations for files

# Motivation

Poor monitoring or poor documentation provokes problems in datacenter operations.

# Usage

`java -cp target/opsd-...-...jar org.zoquero.opsd.App /path/to/project_file.xlsx`

# Output

It generates an HTML page with:

* validation of the file
* warnings and errors
* monitoring script
* mediawiki sources

# Build and install

`cd opsd`
`mvn package`
`cp target/opsd-1.0-SNAPSHOT.jar /wherever/`

## Dependencies

* Maven
* Apache POI

# Source
It's source can be found at GitHub: [https://github.com/zoquero/opsd/](https://github.com/zoquero/opsd/).

