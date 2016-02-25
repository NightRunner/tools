package org.nightrunner.tools;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by NightRunner on 2016-02-24.
 */
public class FileStringReplacer {
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\NightRunner\\IdeaProjects\\bms-foundation\\bms-core\\src\\main\\java\\com\\bmit\\product\\bms\\core\\util\\formula");

        final FilenameFilter filenameFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith(".java")) {
                    return true;
                }
                return false;
            }
        };

        String source = "\t";
        String replacement = " ";

        FileStringReplacer fileStringReplacer = new FileStringReplacer(dir, filenameFilter, source, replacement);
        fileStringReplacer.execute();
    }


    private String source;

    private String replacement;

    private File dir;

    private FilenameFilter fileNameFilter;

    public FileStringReplacer(File dir, FilenameFilter fileNameFilter, String source, String replacement) {
        this.source = source;
        this.replacement = replacement;
        this.dir = dir;
        this.fileNameFilter = fileNameFilter;
    }

    public void execute() {
        Collection<File> files = new LinkedList<File>();
        appendFileName(dir, files);

        for (File file : files) {
            process(file);
        }
    }


    private void process(File file) {
        try {

            List<String> lines = readLines(file);

            processLines(lines);

            writeLines(file, lines);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeLines(File file, Collection<String> lines) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),
                Charset.forName("UTF-8")));

        for (String tmp : lines) {
            writer.println(tmp);
        }

        writer.flush();
        writer.close();
    }

    final int initialCapacity = 100;

    private List<String> readLines(File file) throws IOException {
        List<String> lines = new ArrayList<String>(initialCapacity);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                Charset.forName("UTF-8")));

        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    private void processLines(List<String> lines) {

        String line = null;
        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);

            while (line.contains(source)) {
                line = line.replace(source, replacement);
            }

            lines.set(i, line);
        }

    }

    private void appendFileName(File dir, Collection<File> files) {
        if (dir.isDirectory()) {
            files.addAll(Arrays.asList(dir.listFiles(fileNameFilter)));
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    appendFileName(file, files);
                }
            }
        }
    }
}
