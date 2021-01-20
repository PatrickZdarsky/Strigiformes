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
Strigiformes supports legacy color-codes, and the new RGB colors. It translates all colors to the 



### Variables
Variables are used to avoid having duplicate messages defined. Variables look like the following: `${NAMESPACE:VARIABLE_NAME}`

Normally the tag contains only one value which defines the key. The parser will replace the tag with the translation-string with the specified key.
If multiple TextProviders are created one can access strings from the other TextProvider by specifying the namespace through prefixing the name with it and using a colon to separate the two.

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