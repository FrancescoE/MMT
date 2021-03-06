# Context API

## Description
The ```Context``` API returns an array of the top domains matching the provided content.
For each domain you obtain the consine similarity between the given context, and that domain.

### Example

**Input:**

Text:
```Mr President```

Top domains and scores:
```europarl (0.13375875), ibm (0.008800022)```

## Input / Output definition

**HTTP method:**

``` GET ```

**Input:**

* ```limit```: the max number of returned elements (default is 10)
* ```text```: the context text to be analyzed
* ```local_file```: the absolute path to a local file to be used as context string; if specified, ```text``` is ignored. 

**Output:**

A JSON array of objects with ```id``` and ```score````:

```id``` is the name of the domain

```score``` is the cosine similarity between the domain and the given text

### API call example

```GET context?text=Mr+President```

```json
{
    "data": [
        {
            "id": "europarl",
            "score": 0.13375875
        },
        {
            "id": "ibm",
            "score": 0.008800022
        }
    ],
    "status": 200
}
```
