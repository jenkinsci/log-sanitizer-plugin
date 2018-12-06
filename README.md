# Jenkins Logs Sanitizer Plugin

[![Build Status](https://travis-ci.org/jenkinsci/ansicolor-plugin.svg)](https://travis-ci.org/jenkinsci/ansicolor-plugin)

This plugin allows users to suppress output of enclosing pipelines steps to Console Output.

## Using in pipeline workflows

Also, the following pipeline syntax can be used without allocating a node. 

```groovy
sanitizer(false) {
   echo 'Hello World'
   bat "dir"
}
```

# Contributing

See [CONTRIBUTING](CONTRIBUTING.md).
