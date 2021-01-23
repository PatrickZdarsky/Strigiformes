# Strigiformes 
Strigiformes is a minecraft translation framework. It can convert human-readable localized template strings into fully featured Minecraft ChatComponents.

## Features
- 1.16 RGB Colors
- Color gradients
- MessageFormat formatting
- Full ClickEvent and HoverEvent features
- TextProviderRegistry to access template strings from other namespaces
- Code-Wise ChatComponent creation [WIP]
- Template-String caching
- Utilities to localize various objects

## The template string
The template string is the human-readable form of a ChatComponent message. It is later being parsed to create the ChatComponent.
### Colors
Strigiformes supports legacy color-codes, and the new RGB colors. It translates all colors to the new hex-representation.

Colors can be set in the following ways:
- §d            (Legacy color codes)
- §{white}      Using a predefined name
- §{#224433}    Using hex-values

By default the minecraft built-in names are registered, and the ones declared in the ``java.awt.Color`` class. By adding a new color using ``ColorRegistry.addColor(name, color)`` one can add more values.

#### Color gradients
Color gradients are defined by two color-tags like: 
``§{yellow~}I am a beautiful Gradient§{red}``
These gradients are defined by adding a ``~`` suffix to the color. After this character a specific GradientGenerator can be optionally set. By default the LinearGradient is used.

Example: ``§{yellow~random}Colorful§{red}`` This message will use the RandomGradientGenerator.

Available Generators:
- LinearRgbGradientGenerator
- RandomGradientGenerator
- More to come...


**IMPORTANT:** When enabling the legacy 1.8 mode using ``ColorRegistry.useLegacyColors = true;`` only legacy colors in the form of: `§a` are supported! 
Color-Gradients are also not supported in this mode!

### Variables
Variables are used to avoid having duplicate messages defined. Variables look like the following: `${NAMESPACE:VARIABLE_NAME}`

Normally the tag contains only one value which defines the key. The parser will replace the tag with the translation-string with the specified key.
If multiple TextProviders are created one can access strings from the other TextProvider by specifying the namespace through prefixing the name with it and using a colon to separate the two.

Example:

    prefix=Lobby:
    message=${prefix} Welcome to the server!   

Will produce: ``Lobby: Welcome to the server!``

### Manual ChatComponent definition
To manually create a ChatComponent with a ClickEvent or HoverEvent you have to use the `%{...}` tag.
The content of this tag is separated by `|` characters. You can escape the content delimiter by prefixing it with a \

Example: ``%{§cNotch|run_command:/tp Notch|show_text:Teleport to Notch!}``

This will create the following ChatComponent complete with a ClickEvent and HoverEvent:

    {
        "color": "#ff5555",
        "text": "Notch",
        "hoverEvent": {
          "contents": [
            "",
            {
              "text": "Teleport to Notch!"
            }
          ],
          "action": "show_text"
        },
        "clickEvent": {
          "action": "run_command",
          "value": "/tp Notch"
        }
    }
   
## Todo
- Improve MessageFormat implementation
- Improve Exceptions to display the problematic part better
- Fix issue where when you get a string from another namespace, and the translation-string 
  contains variables the original textProvider should be used, currently the calling one is 
  used which is not able to resolve the variables