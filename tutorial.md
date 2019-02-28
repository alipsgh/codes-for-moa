# Tutorial

### 1. Set up MOA

First of all, we must download and then install a Java IDE such as [Eclipse](https://www.eclipse.org/downloads/), [NetBeans](https://netbeans.org/) and [IntelliJ IDEA](https://www.jetbrains.com/idea/download/). I use Eclipse for the purpose of this tutorial.
Once the IDE of choice is set up, we have to download the MOA framework from its [GitHub Repository](https://github.com/Waikato/moa), then import it into the IDE as a Maven project. In Eclipse, click on `File >> Import... >> Maven >> Existing Maven Project`. Then, click on `Next` and `Browse...` to find unzipped MOA, as follows:

<p align="center">
  <img src="/img/01_maven_1.png" width="50%" />
  <img src="/img/02_maven_2.png" width="50%" />
</p>

If you encounter a wizard for "Setup Maven plugin connectors", simply overlook it by clicking the `Finish` button. MOA's packages should be imported as below:

<p align="center">
  <img src="/img/03_eclipse.png" width="60%" />
</p>

Now, we need to configure settings for running MOA. For that, we need to go to `Run >> Run Configurations...`. Then, set the **Main Class** to `moa.gui.GUI`, or use the `Search...` button, as below:

<p align="center">
  <img src="/img/04_run_1.png" width="60%" />
  <img src="/img/05_run_2.png" width="50%" />
</p>

Once all is set, you can run MOA by clicking the `Run` button. Finally, MOA's GUI shows up:

<p align="center">
  <img src="/img/06_moa_gui.png" width="60%" />
</p>

Everything is now ready for getting our hands dirty!

### 2. Add DDMs

We need to locate a relevant package and put our class in it, it is a simple drag-and-drop. As an example, for my `FHDDM` drift detector, we should add it under the `moa.classifiers.core.driftdetection` package.

<p align="center">
  <img src="/img/07_FHDDM_1.png" width="60%" />
</p>

Once we run MOA again, we will see the added functionality. For example, in my `FHDDM` case, we have:

<p align="center">
  <img src="/img/08_FHDDM_2.png" width="50%" />
</p>

You now have all the nuggets for your experiments. Good luck.

**A Few Points**

* You may need to update some imports as MOA gets updated over time. For example, you may need to change `import moa.options.IntOption;` to `import com.github.javacliparser.IntOption;` for FHDDM.
* Please contact MOA creators for Java and JRE compatibility, or any warning issues.
* Thank you for your interest in my drift detection methods, and I hope they help you in your research or industrial works.

<br/>
<br/>

<sub>Ali Pesaranghader © 2019</sub>
