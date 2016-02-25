# peel-reef-on-yarn-experiment
Test bundle to start a REEF job on YARN using the `YarnExperiment` bean.

# Project Structure
The project consist of three directories that are specific to the bundle: `example-bundle/src`,
`example-bundle/target/example`, and `example-yarn-jobs`. The first two contain the experiment specification and the
bundle binaries respectively. The latter contains the actual code that is run during the experiment.

```
.
├── LICENSE
├── README.md
├── example-bundle
│   ├── example-bundle.iml
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       ├── assembly
│   │       │   ├── bundle.dev.xml
│   │       │   └── bundle.prod.xml
│   │       └── resources   # bundle specification, change when necessary
│   │           ├── bin
│   │           │   └── peel.sh
│   │           ├── config
│   │           │   ├── experiments.xml
│   │           │   └── fixtures
│   │           │       └── systems.xml
│   │           ├── datasets
│   │           │   └── rubbish.txt
│   │           ├── downloads
│   │           │   ├── README.md
│   │           │   └── systems.txt
│   │           ├── utils
│   │           │   ├── README.md
│   │           │   └── sqlscripts
│   │           │       └── scripts.sql
│   │           └── version.txt
│   └── target
│       └── example    # executable bundle binary
├── example-peelextensions
│   ├── ...
├── example-yarn-jobs   # experiment code
│   ├── pom.xml
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── peelframework
│                       └── carabolic
│                           └── yarn
│                               └── hello
│                                   ├── HelloDriver.java
│                                   ├── HelloDriverRestart.java
│                                   ├── HelloJVMOptionsDriver.java
│                                   ├── HelloJVMOptionsREEF.java
│                                   ├── HelloREEF.java
│                                   ├── HelloREEFYarn.java
│                                   ├── HelloREEFYarnRestart.java
│                                   ├── HelloReefYarnTcp.java
│                                   ├── HelloTask.java
│                                   └── package-info.java
└── pom.xml
```

# Usage
To run this bundle it needs to be build first with

```bash
$ mvn package
```

Then change to the binary directory

```bash
$ cd example-bundle/target/example/
```

And finally run the experiment

```bash
$ ./peel.sh exp:run yarn-suite simple-yarn-app
```

# References
See the [Peel Manual](http://peel-framework.org/manual) for more information how to build and run the experiments
bundled in this package.
