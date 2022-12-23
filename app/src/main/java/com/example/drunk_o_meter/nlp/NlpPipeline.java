package com.example.drunk_o_meter.nlp;

// https://blogs.oracle.com/javamagazine/post/java-sentiment-analysis-stanford-corenlp

  //nlpPipeline.java
  import android.util.Log;
  import java.util.Properties;
  import edu.stanford.nlp.pipeline.StanfordCoreNLP;
  import edu.stanford.nlp.ling.CoreAnnotations;
  import edu.stanford.nlp.pipeline.Annotation;
  import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
  import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
  import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
  import edu.stanford.nlp.trees.Tree;
  import edu.stanford.nlp.util.CoreMap;

  public class NlpPipeline {
      static StanfordCoreNLP pipeline;
      public static void init()
      {
          Properties props = new Properties();
          props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
          pipeline = new StanfordCoreNLP(props);
      }
      public static Sentiment estimatingSentiment(String text)
      {
          int sentenceCount = 0;
          int sentimentMean = 0;
          int sentimentInt;
          String sentimentName;
          Annotation annotation = pipeline.process(text);
          for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
          {
              Tree tree = sentence.get(SentimentAnnotatedTree.class);
              sentenceCount++;
              sentimentInt = RNNCoreAnnotations.getPredictedClass(tree);
              sentimentMean = sentimentMean + sentimentInt;
              sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
              Log.d("D-O-M NLP", sentimentName + "\t" + sentimentInt + "\t" + sentence);
          }

          // Return mean sentiment of the analyzed text
          if (sentimentMean == 0){
              Log.d("D-O-M NLP Result", Sentiment.VERY_NEGATIVE.toString());
              return Sentiment.VERY_NEGATIVE;
          } else {
              sentimentMean = Math.round(sentimentMean / sentenceCount);
              Log.d("D-O-M NLP Result", Sentiment.values()[sentimentMean].toString());
              return Sentiment.values()[sentimentMean];
          }

      }
  }
