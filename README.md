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


# License

The ANSI Color Plugin is licensed under the MIT License.

It uses [JANSI](https://github.com/fusesource/jansi/) (Apache 2.0 License).

# Contributing

See [CONTRIBUTING](CONTRIBUTING.md).
