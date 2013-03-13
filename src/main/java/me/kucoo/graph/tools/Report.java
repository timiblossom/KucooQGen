package me.kucoo.graph.tools;


public interface Report {
    void reset();

    void finish();

    void dots();

    void finishImport(String type);
}