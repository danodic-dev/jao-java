# JAO Java Implementation
This is the Java implementation of the [JAO Specification](). You may want to read this specification first to understand some of the basic concepts before exploring the library.

![example workflow](https://github.com/danodic-dev/jao-java/actions/workflows/master.yml/badge.svg)

## Quick Start
Add the `jao-java` library to your project:
```
<dependency>
    <groupId>dev.danodic</groupId>
    <artifactId>jao-java</artifactId>
    <version>1.3.0</version>
</dependency>
```

Also, make sure you have an action library in your classpath as well:
```
<dependency>
    <groupId>dev.danodic</groupId>
    <artifactId>jao-java-standard-library</artifactId>
    <version>1.1.0</version>
</dependency>
```

After that, you can start using it as follows:
```
public class Sample {

    private Jao sampleJao;
    
    public void initialize() {
        Jao sampleJao = JaoBuilder.getJaoFromJson("./sample.jao", MyRenderer.class);
        sampleJao.setEvent("init");
    }
    
    public void render() {
        sampleJao.render();
    }

}
```

You can use the `JaoBuilder` class to provide you an instance of the `Jao` class, that is the main jao class. You have to provide an implementation of `IRenderer` to this class, so that the jao library knows what to do when events are triggered. After that, you can set your event using `setEvent` and call `render` in your rendering/logic cycle.

# Basic Concepts
The following diagram provides a high-level understanding of the basic components of the java implementation of *jao*:

![image](https://user-images.githubusercontent.com/6846892/152671208-52d8d76a-4b5b-4975-bbca-c3409fb489c9.png)

## The jao files
A jao file can be either a `.jao` (a `.zip` renamed to `.jao`), a `.zip` or a folder. The `jao-java` library can parse either one of those formats based on the extension of the file provided to the loader. Custom implementations of the `IExtractor` interface can be used to allow other ways of loading files (from different data types or from different places -- from memory or from a URL, for example).

## `jao-java` library
The main library is used for:
- Parsing and loading the jao files;
- Managing and executing events and actions;
- Interfacing with the renderer using the layers.

## `actions library`
The action library is responsible for executing the actions you find inside the json -- things like `TranslateOverTime` or `SetPosition`. It is expected that the user should be able to have multiple action libraries loaded into a single project if needed -- for that reason, the `jao-java` code will scan the classpath for classes that implements `IAction` and `IInitializer` during the initialization of the code. As mentioned above, you can use the `jao-java-standard-library` as your default source of actions, and can add custom actions if needed.

## `IRenderer`
The `IRenderer` inteface has to be implemented by the user. Each layer inside the jao file will be tied to one instance of the renderer, and that renderer will define how the layer will be rendered on the screen. It is reponsibility of the renderer to read the layer parameters and transform them into whatever code fits the graphics library being used.

## Rendering
The implementation of `jao-java` is a blocking-implementation -- you need to call `Jao::render` each time you want to update the state of the events and actions. The time is based on real time (*`second 1` means 1 second* -- no more and no less) but the evaluation of the actions based on the current time is only done when `render()` is called. The time-management can be changed by implementing a new `IClock` (more details below).

# Implementing a Renderer
Unless you are using a renderer implementation that already exists, you will proabably want to write your own renderer. The renderer will effectively talk to the library/graphics framework you are using in your application. A popular one for Java is LibGDX for example.

## Anatomy of an Action
An action will be created automatically by the `jao-java` library, and it will manipulate values in memory. What those values are will depend on what the action does, but ultimately it will store, update and delete values from the parameters of the current layer. Those are some examples:
- `position_x`
- `scale_y`
- `opacity`
Those values are usually just numbers, that will serve as an input for the renderer. The action will not execute nothing that will draw into the screen, the renderer is responsible of materializing those abstract numbers into actual information on your screen.

## Layers and Layer Parameters
As seen in the [JAO specification](), each leayer repsents a data type. It can be an image, a text or anything else -- the renderer is supposed to understand what this data type is and materialize it on the screen. Each layer as a list of parameters that have to be used by the renderer to change the aspect of this layer on the screen. That list of parameters is basically a `Map`, where the keys are strings and the values can be anything.

So basically, your Renderer will read a `HashMap` to get the values updated by the actions so it can apply those values to the actual graphical element on the screen.

## The `IRenderer` interface
The `IRenderer` interface will be used to materialize the layer into the screen. This class is passed as an argument to the `JaoBuilder` class, as follows:
```
Jao sampleJao = JaoBuilder.getJaoFromJson("./sample.jao", MyRenderer.class);
```
As the parser will instantiate the renderer for each layer in runtime, your renderer implementation needs to have a constructor with no arguments.

### The renderer methods
The `IRenderer` interface implements the following methods:

#### `setDataType`
```
void setDataType(DataTypeModel dataType, IExtractor extractor);
```
The `DataTypeModel` class will contain exactly the same data that you define in the `jao.json` for a given layer. That means it will provide you the `dataType` (which will be something like `sprite`, `animation`, `sample`, etc.) and a `Map` with the list of custom attributes provided.

The `IExtractor` implementation is responsible for allowing you to fetch data from the jao file, regardless of how it is stored. It is your responsibility to request the file from the extractor based on the filename provided in the `DataTypeModel`.

The method `setDataType` is invoked during the parse of the jao file, so you may not want to do a lot of stuff here -- it may block the parsing process and that will drastically slow down the loading process of your files in case you are loading a lot of jao files at a given time (ex.: during the initial loading of your game). A suggestion is to use this method to store data, and call the `initialize()` method later when it is more convenent.

You can read any file inside the jao file using the extractor, and your layers can even fetch more than one file.

#### `initialize`
As mentioned above, you don't have control over when `setDataType` is invoked -- it always happens during the parsing process. So if you want to initialize your renderer at a specific time, you can implement your code inside the `initialize` method:
```
void initialize(Object ... args);
```
This method will execute whatever code you need to initialize your renderer, and you can provide any argument to it. You can invoke this method in two ways:
- Acessing the each layer and calling it: `jao.getLayers().forEach(l->initialize())`
- Or by calling it from the `Jao` class: `jao.initializeLayers()`

#### `render`
This is where the magic happens. The `render` method is where you are supposed to read the data from the layer and render things on screen:
```
void render(JaoLayer layer, Object ... args);
```
Aside from the layer, you can provide any argument to the `render` method -- ex.: when using LibGDX you can provide your `BatchRenderer`.


void dispose();
IRenderer clone();
void debug();
boolean isText();
IText getTextRenderable();
```

## Example: a LibGDX renderer


# Extending
# Implementing an Extractor
# Implementing your own actions

# Inner Workings
The diagram below serves as a reference of the overall structure of the library:

![image](https://user-images.githubusercontent.com/6846892/152671666-4cb721be-e88f-46f7-b6f6-48b214bd4fa8.png)
