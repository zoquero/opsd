# Summary

**Operations Descriptor** is a tool to generate monitoring and documentation of a Project and its resources, useful in datacenter operations if there's still not a CMDB.

Angel Galindo Muñoz ( zoquero@gmail.com ), November of 2018

# Features

In this release the input is an Excel file describing project, hosts, services, procedures, ... in a future release the input may be a relational database or even a CMDB.

* It validates the input (an Excel file)
* It generates:
    * debugging information about itself (useful for troubleshooting opsd)
    * hyperlinked mediawiki code describing:
        * inventory
        * procedures for incidents: usually triggered by monitoring
        * procedures for requestable tasks: usually triggered by ticketing
        * periodic operations: tasks triggered by time
        * lifecycle operations for files: just a tip to try to persuade people to do housekeeping of their own files
    * script to generate monitoring: it allows you to construct calls to custom commands "addHost" / "addService" that you have to implement by yourself for your monitoring tool. For example I'm using Centreon's CLAPI, heavy-using service templates and host templates.

# Motivation

Poor monitoring or poor documentation provokes problems in datacenter operations without CMDB

# Output

It generates an HTML page with:

* validation of the file
* warnings and errors
* mediawiki sources
* monitoring script

# Build and install

It's recommended, but not indispensable, to be build using Maven to get the version of Opsd on build time. Recommended because we're getting project version from the pom.xml file.

`cd opsd`

`mvn package`

`cp target/opsd-1.1-SNAPSHOT-jar-with-dependencies.jar /wherever/`

## Source
It's source can be found at GitHub: [https://github.com/zoquero/opsd/](https://github.com/zoquero/opsd/).

## Dependencies

* Apache POI

# Usage

* (optional) Set locale, to select the localized Freemarker template if the desired locale differs from the current locale. Now there are just two templates for projectes: English and Catalan. Add templates for more languages. For example: to use Catalan language and display using src/main/resources/org/zoquero/opsd/templates/project_ca_ES.ftl before running opsd you must do:
`export LANG=ca_ES.UTF-8`

* Execution:
`java -cp target/opsd-...-jar-with-dependencies.jar org.zoquero.opsd.App /path/to/project_file.xlsx 'projectName'`

Note: Even though the project name may appear in the project data you must specify it in the call. This is because the output folder is created before reading the project data and it contains the project name to ease its access. That project name in the call is just to name the folder, so if it's a problem for you just specify 'SomeProject'.

# Sample files:

* `extra/sample_project_files/empty_project_file.xlsx` : An empty project spreadsheet file

* `extra/sample_project_files/Hola, món.xlsx` : A sample project spreadsheet file

* `extra/sample_project_files/ProjectInfo.Hola_mon.8053360294583847439` : Generated output for the sample project 'Hola, món'.
