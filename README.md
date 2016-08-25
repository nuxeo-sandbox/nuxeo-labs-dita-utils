# About **nuxeo-labs-dita-utils**

A Nuxeo Package for dealing with [DITA](http://dita.xml.org/) content.

## Dependencies

These must be installed on the machine running Nuxeo and the `dita` command must be available on the path.

* [DITA Open Toolkit](http://www.dita-ot.org/)
* [DITA to Word converter](https://github.com/jelovirt/com.elovirta.ooxml)

## Usage

Operations:

* DITA.ZippedDita2DocXOp - Given a `zip` file that contains a DITA project, this operation will extract the zip and convert the project to `DOCX`. Note that only a single `ditamap` file is supported.

## Building

After cloning, and assuming `maven` is correctly installed:

`mvn install`

The Nuxeo Package will be placed at `nuxeo-labs-dita-utils-pacakge/target/`.

## Support

**These features are not part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.

## Licensing

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

## About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Netflix, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.

More information is available at [www.nuxeo.com](http://www.nuxeo.com).
