<!--
    Original template from:
    https://claude.ai/share/2539339f-ea2e-4429-893e-e851c1a7a76f
    [24/03/2025] Revised using template from:
    https://claude.ai/share/5289331f-ee0b-4e37-a5a6-641745b15808

    By: Jorge Arévalo
-->

```mermaid
classDiagram
    User "1" *-- "0..*" Note : owns
    User "1" *-- "0..*" Tag : has
    Note "0..*" *-- "0..*" Tag : is categorized by

    NodeParent "1" *-- "0..*" ItemNode : contains

    BaseNode <|-- ImageNode
    BaseNode <|-- ParagraphNode
    BaseNode <|-- ListNode
    ItemNode <|-- BaseNode
    NodeParent <|-- Note
    NodeParent <|-- ListNode
    ListNode -- ListType : is of type

    class User {
        id: UUID
        firstName: String
        lastName: String
        email: String
        password: String
        registrationDate: Date
    }

    class Note {
        id: UUID
        title: String
        creationDate: Date
        lastModifiedDate: Date
        isDeleted: boolean
    }

    class Tag {
        id: UUID
        name: String
        color: Color
    }

    class BaseNode {
        <<abstract>>
        id: UUID
    }

    class ParagraphNode {
        text: String
    }

    class ImageNode {
        image: Image
        caption: String
    }

    class ListType {
        <<enumeration>>
        BULLETED
        NUMBERED
    }
    
    class NodeParent {
        <<interface>>
    }
    
    class ItemNode {
        <<interface>>
        orderIndex: int
    }
```