# simple-document-search

Hi.

This simple search engine is a command line app. After starting it, you will be asked for a path
to a file containing document list. The app treats every line in the file as a single document - 
i know it is a simplified solution, but the task did not include any requirements or information about the way
the documents should be loaded.

After the file has been loaded you can query the engine by writing the word (token) you want to search for,
the programm will output all documents containing the token, sorted by TF-IDF in descending order.

I have written some tests for my own sake, you can use them to test the app.

Furthermore, while writing the app I focused on parallelization of operations - that's why some parts of the code
are more complicated then they need to be, since i tried to keep most of the data immutable.
One exception is 
``` .peek(placeWordsInGlobalCount(numOfDocumentsWithWord)) ```

I did it this way to avoid excess iterations through the files.


## !!One last thing - I think you have an error in your recruitment task!!

In the given example, 
```
The following documents are indexed: 
Document 1: “the brown fox jumped over the brown dog” 
Document 2: “the lazy brown dog sat in the corner” 
Document 3: “the red fox bit the lazy dog” 

A search for “brown” should now return the list: [document 1, document 2]. 
A search for “fox” should return the list: [document 1, document 3]
```
You expect that the documents will be sorted by td-idf (i took the assumption that the order should be descending)
The search for brown is fine, but i think the search for fox is wrong.
Calculating the term frequency:
```
tf("fox", d1) = 1/8 = 0.125
tf("fox", d3) = 1/7 = 0.143
```
Calculating the inversed document frequency:
```
idf("fox", D) = log(3/2) = 0.176
```

Calculating TD-IDF:
```
tfidf("fox", d1, D) = 0.125 * 0.176 = 0.022
tfidf("fox", d3, D) = 0.143 * 0.176 = 0.025
```

Hence the document 3 should be first in the list since 0.025 > 0.022

I may of course be missing something, so please correct me if I am wrong.

