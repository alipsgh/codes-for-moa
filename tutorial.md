# Tutorial

### 1. Set up MOA

First of all, we must download and then install a Java IDE such as [Eclipse](https://www.eclipse.org/downloads/), [NetBeans](https://netbeans.org/) and [IntelliJ IDEA](https://www.jetbrains.com/idea/download/). I use Eclipse for the purpose of this tutorial.
Once the IDE of choice is set up, we have to download the MOA framework from its [GitHub Repository](https://github.com/Waikato/moa), then import it into your IDE as a Maven project. For that click on `File >> Import... >> Maven >> Existing Maven Project`. Then click `Next` and `Browse...` MOA's the unzipped folder, as follows:

<p align="center">
  <img src="/img/01_maven_1.png" width="50%" />
  <img src="/img/02_maven_2.png" width="50%" />
</p>

If you encounter a wizard for "Setup Maven plugin connectors", simply overlook it by clicking the `Finish` button. MOA packages are imported as shown below:

<p align="center">
  <img src="/img/03_eclipse.png" width="60%" />
</p>

Now, we need to configure settings for when we want to MOA. For that, you need to go to `Run >> Run Configurations...`. Then set the **Main Class** to `moa.gui.GUI`, or use the `Search...` button, as below:

<p align="center">
  <img src="/img/04_run_1.png" width="60%" />
  <img src="/img/05_run_2.png" width="50%" />
</p>

Once all is set, you can run MOA by clicking the `Run` button. Then, MOA's GUI shows up:

<p align="center">
  <img src="/img/06_moa_gui.png" width="60%" />
</p>

### 2. Add DDMs

