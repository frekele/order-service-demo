package br.com.demo.application.core;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);
}
