# Summary

**Operations Descriptor** is a tool to generate monitoring and documentation of a Project (defined in an Excel file) and its resources, usefull in datacenter operations.

Angel Galindo Muñoz ( zoquero@gmail.com ), November of 2018

# Features

It validates the Excel file.

It generates:
* script to generate monitoring (based on a "addHost" and "addService" API)
* mediawiki documents with:
    * inventory
    * procedures for incidences
    * procedures for requests
* periodic operations
* lifecycle operations for files

# Motivation

Poor monitoring or poor documentation provokes problems in datacenter operations.

# Usage

`java -cp target/opsd-1.0-SNAPSHOT.jar:/your/jars/poi-ooxml-3.15.jar:/your/jars/poi-3.15.jar:/your/jars/xmlbeans-2.6.0.jar:/your/jars/commons-collections4-4.1.jar:/your/jars/poi-ooxml-schemas-3.15.jar:/your/jars/freemarker-2.3.28.jar org.zoquero.opsd.App /path/to/project_file.xlsx 'projectName'`

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

