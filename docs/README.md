# MMT Reference

### REST API

- [translate.md](rest-api/translate.md): The ```Translate``` API returns the translation of the provided text. The text is XML encoded and **can contain XML Tags**.

- [context.md](rest-api/context.md): The ```Context``` API returns an array of the top domains matching the provided content.
For each domain you obtain the consine similarity between the given context, and that domain.

- [tags-projection.md](rest-api/tags-projection.md): The ```Tags projection``` API takes as input a source sentence and its translation and gives as output the translation with the tags projected from the source sentence.

