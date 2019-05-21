# documentscanner (not finished yet)
Working on Edge Detection without using Canny

# Idea
Use random forest to detect edges then i try to use houghlines to get the best... 

# How to use
Top-level gradle:
    <code>allprojects {
      repositories {
          ...
          maven { url "https://jitpack.io" }
        }
    }</code>
  
Then add this line to project gradle:
  <code>implementation 'com.github.haocse.documentscanner:documentscanner:1.0.2'</code>

# Reference
    https://blogs.dropbox.com/tech/2016/08/fast-and-accurate-document-detection-for-scanning/
