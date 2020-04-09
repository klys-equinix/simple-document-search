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

