lazyglot
========

A tool to automatically generate [Anki](http://ankisrs.net) flashcards from Japanese text.  Currently Japanese -> English only.  It's at a very basic state of functionality, but if you mess with it you can get it to work with your own Japanese input.

More to come soon.

Future Plans
------------

- support for other languages (mainly dependent on existence of well-made open source dictionaries)
- direct generation of Anki cards.  Currently only a CSV file is generated, which can easily be imported into Anki

Software used
-------------

lazyglot is mostly just a bunch of glue connecting a few pieces of excellent open source software:

- [Kuromoji](http://atilika.org/kuromoji/) for word segmentation
- [JMdict](http://www.csse.monash.edu.au/~jwb/edict_doc.html) for word lookup
- [Anki](http://ankisrs.net) for flashcards