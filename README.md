# Donatello: Flow-based Programming IDE

[![Release](https://jitpack.io/v/MarginallyClever/Donatello.svg)](https://jitpack.io/#MarginallyClever/Donatello)

A pure Java implementation of [Flow-based Programming](https://en.wikipedia.org/wiki/Dataflow_programming) (FBP) and a GUI editor.

![img](docs/preview-for-github.png)

Data-flow programming is aesthetically pleasing, greatly reduces the chance of syntax error, and empowering for people
that are not fluent in the archaic syntax of text-only languages.

## Features

- Nodes are not directed or forced to run via triggers.  There is little danger of large networks overflowing the stack.  They could be run in parallel.
- Folding: Collapse a subgraph down to a single Node with *Fold* and reverse with *Unfold*
- The editor has written in Java Swing.  The main executable class is `com.marginallyclever.donatello.Donatello`.
- A ~/Donatello/ folder contains the application log file.
- A ~/Donatello/extensions/ folder contains 3rd party plugins.  Add new Nodes or write your own.
- While running the editor you can also access Swing-only nodes like `LoadImage` and `PrintImage`.  PrintImage results will appear in the background of the node editor panel.
- Convenient built-in nodes for basic math and reporting.
- Unit tests for everything!  If it can be tested, we shall!

### Getting started

1. Download NodeGraphCore (https://github.com/MarginallyClever/NodeGraphCore/)
2. Use your favorite IDE to import the Maven project.
3. Use Maven to "install" the project.  It will now be available as a local dependency in your other projects.
4. Repeat these steps for Donatello (https://github.com/MarginallyClever/Donatello/)
5. Donatello can be built to run on its own, or as a plugin in your project.

./src/test/java/com/marginallyclever/donatello has unit tests, which are also examples of how to use the API.

## Use it, Discuss it, Love it.

- Please see the [Javadoc with the full node graph API](https://marginallyclever.github.io/Donatello/javadoc).
- Please see guide for [how to Contribute](https://github.com/MarginallyClever/Donatello/blob/main/CONTRIBUTING.md)
- The [Official webpage](https://marginallyclever.github.io/Donatello/)!
- Join [the Discord channel](https://discord.gg/Q5TZFmB) and make new friends.

## Based on work by

- https://github.com/otto-link/GNode/
- https://github.com/jpaulm/javafbp/
- https://nodes.io/story/
- https://github.com/janbijster/cobble
- https://github.com/kenk42292/shoyu
- https://github.com/paceholder/nodeeditor
- https://github.com/miho/VWorkflows
- https://nodered.org/
- Maya, Unity, Blender
- NoFlo, Flowhub
- and others

## Learn more

- [Flow based programming Discord](https://discord.com/invite/YBQj6UsD5H)
- https://jpaulm.github.io/fbp/

## Icons

Many app icons provided by http://icons8.com.