---
layout: page
title: New Wiki Page Tutorial
permalink: /wiki/new-wiki-page-tutorial/
---

To make a new wiki post, create a markdown file in the `docs/wiki` folder. Call it whatever you want, with spaces represented by dashes. For example, this file's name is `new-page-tutorial.md`.

Next, create a front-matter header. This should be the first text in your file. Here's the general form:
```
---
layout: page
title: Whatever Title You Want
permalink: /wiki/whatever-title-you-want/
---
```

Now you can use [regular markdown syntax](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet) to make your wiki webpage.

Once your page is complete, make sure to create a link to it in the `wiki.md` file. Here's the link to this page:
```
To make a new wiki page, [go here.](./new-wiki-page-tutorial/)
```

And that's it, all you have to do is push to GitHub and now your file is on the Wiki.

