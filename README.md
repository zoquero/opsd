# Summary

**Operations Descriptor** is a tool to generate monitoring and documentation of a Project and its resources, usefull in datacenter operations if there's still not a CMDB.

Angel Galindo Muñoz ( zoquero@gmail.com ), November of 2018

# Features

In this release the input is an Excel file describing project, hosts, services, procedures, ... in a future release the input may be a relational database.

* It validates the input (an Excel file)
* It generates:
    * debugging information about itself
    * mediawiki code describing
        * inventory
        * procedures for incidents
        * procedures for requests
        * periodic operations
        * lifecycle operations for files
    * script to generate monitoring (it allows you to construct calls to custom commands "addHost" / "addService" of your own)

# Motivation

Poor monitoring or poor documentation provokes problems in datacenter operations without CMDB

# Output

It generates an HTML page with:

* validation of the file
* warnings and errors
* mediawiki sources
* monitoring script

# Build and install

It's recommended, but not indispensable, to be build using Maven to get the version of Opsd on build time.

`cd opsd`
`mvn package`
`cp target/opsd-1.1-SNAPSHOT-jar-with-dependencies.jar /wherever/`

# Usage

* (optional) Set locale, to select the localized Freemarker template if the desired locale differs from the current locale. Assegura't prèviament que existeixi la plantilla traduida per a tal idioma (ex.: src/main/resources/org/zoquero/opsd/templates/project_ca_ES.ftl):
`export LANG=ca_ES.UTF-8`

* Execution:
`java -cp target/opsd-1.1-SNAPSHOT-jar-with-dependencies.jar org.zoquero.opsd.App /path/to/project_file.xlsx 'projectName'`

## Dependencies

* Apache POI

# Source
It's source can be found at GitHub: [https://github.com/zoquero/opsd/](https://github.com/zoquero/opsd/).

