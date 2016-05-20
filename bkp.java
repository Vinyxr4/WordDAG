

import edu.princeton.cs.algs4.*;
import java.util.Arrays;

public class WordLadder {
    private static int n = 0;
    private static int[] accum;
    
    // return true if two strings differ in exactly one letter
    public static boolean isNeighbor (String a, String b) {
        if (a.length() == b.length()) {
            int differ = 0;
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) differ++;
                if (differ > 1) return false;
            }
        }
        else {
            String tempA, tempB;
            tempA = a; tempB = b;
            if (a.length () > b.length ()) {
                tempA = tempB; tempB = a;
            }
            if (tempB.length () != 1 + tempA.length () || !tempB.startsWith (tempA))
                return false;
        }
        return true;
    }

    public static Graph Neib (IndexSET<String> words, int len, Graph G) {
        int size = 0;
        String[] strings = new String[10];
        int[] pos = new int[10];

        for (String word1 : words.keys ()) {
            if (word1.length () == len) {
                strings[size] = word1;
                pos[size] = size;
                size++;
            }
        }

        int sft = size;

        while (sft >= 0) {

            for (int j = 0; j < size - 1; ++j) {
                for (int k = j + 1; k < size; ++k) {
                    if (strings[j].substring (0, len - 1).compareTo (strings[k].substring(0, len - 1)) == 0)
                        StdOut.println (pos[j] + accum[len - 1] + " " + pos[k] + accum[len - 1]);
                    else 
                        break;
                }
            }

            shift (strings, pos, size);
            sft--;
        }        
        return G;
    }

    // Orneda uma lista de strings e um vetor que representa as posicoes
    // originais das strings na lista
    public static void sorting (String[] strings, int[] pos, int size) {
        for (int i = 0; i < size - 1; ++i) {
            for (int j = i + 1; j < size; ++j) {
                if (strings[i].compareTo (strings[j]) > 0) {
                    String temp = strings[i];
                    int tempNum = pos[i];
                    strings[i] = strings[j]; strings[j] = temp;
                    pos[i] = pos[j]; pos[j] = tempNum;
                }
            }
        }
    }

    // Realiza shiit circular nas strings, mantendo ordenacao lexicografica
    public static void shift (String[] strings, int[] pos, int size) {
        for (int j = 0; j < size; ++j) {
            String temp = strings[j].substring(strings[j].length () - 1, strings[j].length ());
            strings[j] = temp.concat (strings[j].substring (0, strings[j].length () - 1));
        }
        sorting (strings, pos, size);
    }

    // Cria o grafo das palavras
    public static Graph createGraph (IndexSET<String> words) {

        Graph G = new Graph(words.size ());

        G = Neib (words, 5, G);

        for (String word1 : words.keys())
            for (String word2 : words.keys())
                if (word1.compareTo(word2) < 0 && isNeighbor(word1, word2))
                    G.addEdge(words.indexOf(word1), words.indexOf(word2));
        
        return G;
    }

    // Le as palavras de um texto agrupando as palavras de diferente
    // comprimento em indices distintos de um vetor de IndexSETs
    public static IndexSET readText (IndexSET words, In in) {
        
        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length () > n)
                n = word.length ();
            words.add(word);
        }

        StdOut.println (n);
        accum = new int[n];
        accum[0] = 0;
        for (String word : words)
            accum[word.length ()]++;

        for (int i = 1; i < n; ++i)
            accum[i] += accum[i - 1]; 

        for (int i = 0; i < n; ++i)
            StdOut.println (accum[i]);

        return words;
    } 

    // Verifica se ha caminho de 'from' ate 'to'
    public static void connect (Graph G, IndexSET<String> words, String from, String to) {
        
        if (!words.contains(from)) throw new RuntimeException(from + " is not in word list");
        if (!words.contains(to))   throw new RuntimeException(to   + " is not in word list");

        BreadthFirstPaths bfs = new BreadthFirstPaths(G, words.indexOf(from));
        if (bfs.hasPathTo(words.indexOf(to))) {
            StdOut.println(bfs.distTo(words.indexOf(to)));
            for (int v : bfs.pathTo(words.indexOf(to))) {
               StdOut.println(words.keyOf(v));
            }
        }
        else StdOut.println("NOT CONNECTED");
        StdOut.println();
    }

    // Modulo principal
    public static void main(String[] args) {
        In in = new In(args[0]);
        IndexSET<String> words = new IndexSET<String> ();
        
        words = readText (words, in);

        Graph G = createGraph (words);

        while (!StdIn.isEmpty()) {
            String from = StdIn.readString();
            String to   = StdIn.readString();
            connect (G, words, from, to);
        }
    }
}
    /*// Redimensiona um vetor de IndexSETs
    public static IndexSET[] resize (IndexSET[] words, int N) {
        IndexSET<String>[] NewWords = new IndexSET[N];

        for (int i = 0; i < N; ++i)
            NewWords[i] = new IndexSET<String>();

        for (int i = 0; i < n; ++i)
            NewWords[i] = words[i];

        return NewWords;
    }
    */