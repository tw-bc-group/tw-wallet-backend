# 13. json-ld格式描述vc

Date: 2020-12-24

## Status

2020-12-24 proposed

## Context

使用json-ld格式描述vc，再次说明json-ld是什么，如何使用，有哪些库

### 概念澄清

#### 知识库，语义网络，语义网，知识图谱

1. Knowledge Base：通常翻译为“知识库”。知识库是人工智能的经典概念之一。最早是作为专家系统（Expert System）的组成部分，用于支持推理。知识库中的知识有很多种不同的形式，例如本体知识、关联性知识、规则库、案例知识等。相比于知识库的概念，知识图谱更加侧重关联性知识的构建，如三元组。
2. The Semantic Web ：通常翻译为“语义网”或“语义互联网”，是Web之父Tim Berners Lee于1998年提出的【1】。语义互联网的核心内涵是：Web不仅仅要通过超链接把文本页面链接起来，还应该把事物链接起来，使得搜索引擎可以直接对事物进行搜索，而不仅仅是对网页进行搜索。谷歌知识图谱是语义互联网这一理念的商业化实现。也可以把语义互联网看做是一个基于互联网共同构建的全球知识库。
3. Linked Data：通常翻译为“链接数据”。是Tim Berners Lee于2006年提出，是为了强调语义互联网的目的是要建立数据之间的链接，而非仅仅是把结构化的数据发布到网上。他为建立数据之间的链接制定了四个原则【2】。从理念上讲，链接数据最接近于知识图谱的概念。但很多商业知识图谱的具体实现并不一定完全遵循Tim所提出的那四个原则。
4. Semantic Net/ Semantic Network：通常翻译为“语义网络”或“语义网”，这个翻译通常被与Semantic Web的翻译混淆起来，为了以示区别，这里采用“语义网络”的翻译。语义网络最早是1960年由认知科学家Allan M. Collins作为知识表示的一种方法提出【3】。WordNet是最典型的语义网络。相比起知识图谱，早期的语义网络更加侧重描述概念以及概念之间的关系，而知识图谱更加强调数据或事物之间的链接。
5. Ontology：通常翻译为“本体”。本体本身是个哲学名词。在上个世纪80年代，人工智能研究人员将这一概念引入了计算机领域。Tom Gruber把本体定义为“概念和关系的形式化描述”【4】。通俗点讲，本体相似于数据库中的Schema，主要用来定义类和关系，以及类层次和关系层次等。OWL是最常用的本体描述语言。本体通常被用来为知识图谱定义Schema。
6. 知识图谱: 链接数据也被当做是语义网技术一个更简洁，简单的描述。当它指语义网技术时，它更强调“Web”，弱化了“Semantic”的部分。对应到语义网技术栈，它倾向于使用RDF和SPARQL（RDF查询语言）技术，对于Schema层的技术，RDFS或者OWL，则很少使用。链接数据应该是最接近知识图谱的一个概念，从某种角度说，知识图谱是对链接数据这个概念的进一步包装。上面，我们用平实的语言给出了知识图谱的定义和组织形式。用更正式的说法，知识图谱是由本体（Ontology）作为Schema层，和RDF数据模型兼容的结构化数据集。本体本身是个哲学名词，AI研究人员于上个世纪70年代引入计算机领域。Tom Gruber把本体定义为“概念和关系的形式化描述”，分别指实体的类层次和关系层次。

#### RDF

RDF(Resource Description Framework)，即资源描述框架，其本质是一个数据模型（Data Model）。它提供了一个统一的标准，用于描述实体/资源。简单来说，就是表示事物的一种方法和手段。RDF形式上表示为SPO三元组，有时候也称为一条语句（statement），知识图谱中我们也称其为一条知识

#### RDF的序列化方法

RDF的表示形式和类型有了，那我们如何创建RDF数据集，将其序列化（Serialization）呢？换句话说，就是我们怎么存储和传输RDF数据。目前，RDF序列化的方式主要有：RDF/XML，N-Triples，Turtle，RDFa，JSON-LD等几种。

#### RDF的表达能力

RDF的表达能力有限，无法区分类和对象，也无法定义和描述类的关系/属性。我的理解是，RDF是对具体事物的描述，缺乏抽象能力，无法对同一个类别的事物进行定义和描述。就以罗纳尔多这个知识图为例，RDF能够表达罗纳尔多和里约热内卢这两个实体具有哪些属性，以及它们之间的关系。但如果我们想定义罗纳尔多是人，里约热内卢是地点，并且人具有哪些属性，地点具有哪些属性，人和地点之间存在哪些关系，这个时候RDF就表示无能为力了。不论是在智能的概念上，还是在现实的应用当中，这种泛化抽象能力都是相当重要的；同时，这也是知识图谱本身十分强调的。RDFS和OWL这两种技术或者说模式语言/本体语言（schema/ontology language）解决了RDF表达能力有限的困境。

RDFS(RDF Schema)/OWL(W3C Web Ontology Language)本质上是一些预定义词汇（vocabulary）构成的集合，用于对RDF进行类似的类定义及其属性的定义。

![](../images/owl.jpg)

#### schema.org
Schema.org is a collaborative, community activity with a mission to create, maintain, and promote schemas for structured data on the Internet, on web pages, in email messages, and beyond.
Schema.org vocabulary can be used with many different encodings, including RDFa, Microdata and JSON-LD.
Founded by Google, Microsoft, Yahoo and Yandex, Schema.org vocabularies are developed by an open community process, using the public-schemaorg@w3.org mailing list and through GitHub.


#### Linked data

Linked Data is a way to create a network of standards-based machine interpretable data across different documents and Web sites. It allows an application to start at one piece of Linked Data, and follow embedded links to other pieces of Linked Data that are hosted on different sites across the Web.|

#### json-ld

JSON-LD is a lightweight syntax to serialize Linked Data in JSON [RFC8259]. Its design allows existing JSON to be interpreted as Linked Data with minimal changes.

JSON-LD give your data context，JSON-LD is just a syntax. You need a vocabulary to use it. You could of course create your own vocabulary, but then you can’t expect that others make use of it. So typically you’ll want to use an existing vocabulary. Schema.org is a popular one, but there are many more. -- It depends on your use case.


1. JSON-LD 是一种完整的 RDF 格式。
2. @context 键为类型和属性建立了词汇表。
3. 属性被表示为 JSON 字段，但名称以 @ 开头的属性除外；这些属性具有特殊含义。 @id 和 @type 字段分别提供了资源 ID 和类型。
4. 资源间的关系是通过以 JSON 对象作为字段值来表示的，这会形成一种与 HTML 类似的嵌套结构。
5. 多个属性是使用 JSON 列表来表示的

##### json-ld Core markup
```
1. Types
    a. Object types 这些在schema.org里面有定义
        i. Person
        ii. Place
        iii. Events
    b. Data types 
        i. Date
        ii. Integer
        iii. Temperature
        iv. Floating points
2. Aliasing
    a.  @context{id:"@id"}，方便js解析，否则只能这么使用obj['@id'],现在可以obj.id
3. Nesting
    a. graph 使用属性关联到其他data
        i. Embedding
        ii. Referencing

4. Language，支持多语言
```

#### 如何定义上下文

请参考：[上下文语法](https://www.w3.org/TR/json-ld/#the-context)

```json
{
  "@context": {
    "name": "http://schema.org/name",
    ↑ This means that 'name' is shorthand for 'http://schema.org/name'
    "image": {
      "@id": "http://schema.org/image",
      ↑ This means that 'image' is shorthand for 'http://schema.org/image'
      "@type": "@id"
      ↑ This means that a string value associated with 'image'
        should be interpreted as an identifier that is an IRI
    },
    "homepage": {
      "@id": "http://schema.org/url",
      ↑ This means that 'homepage' is shorthand for 'http://schema.org/url'
      "@type": "@id"
      ↑ This means that a string value associated with 'homepage'
        should be interpreted as an identifier that is an IRI 
    }
  }
}
```

#### 如何使用schema.org和json-ld去创建VC

1. 决定使用哪种schema类型，如果没有合适的可以自己创建，比如新冠检测
2. 填充内容，可以使用schema类型map到字段值
3. 选择一种表达格式，可以是json-ld，也可以是其他格式

#### 有哪些库可以解析Json-ld

1. [json-ld语言支持](https://json-ld.org/#developers)

## Decision

Decision here...

## Consequences

Consequences here...

## 参考文档


1. [知识图谱基础之RDF，RDFS与OWL](https://zhuanlan.zhihu.com/p/32122644)
2. [What is the relationship between RDF, RDFa, Microformats and Microdata](https://stackoverflow.com/questions/14307792/what-is-the-relationship-between-rdf-rdfa-microformats-and-microdata)
3. [语义网络，语义网，链接数据和知识图谱](https://zhuanlan.zhihu.com/p/31864048)
4. [科普 | 知识图谱相关的名词解释](https://mp.weixin.qq.com/s?__biz=MzU2NjAxNDYwMg==&mid=2247484090&idx=1&sn=cc9886b3a7820b3aa0ffe239566dc5e0&chksm=fcb3a55fcbc42c49404bb4cca85c78980d0ecc60e76b75f825ac24a194116d297761ee0a308b&scene=0#rd)
5. [什么是知识图谱?](https://zhuanlan.zhihu.com/p/71128505)
6. [json-ld相关标准](https://www.w3.org/TR/json-ld/)