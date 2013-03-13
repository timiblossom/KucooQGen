package me.kucoo.graph.rule;

import java.util.List;
import java.util.Set;

import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;

public interface IRule {
	public QuestionEntity generate(String keyword);
	public QuestionEntity generate(String keyword, Set<String> excludedSet);
	public List<QuestionEntity> generate(String keyword, int n);
	public List<QuestionEntity> generate(String keyword, int n, Set<String> excludedSet);
	public QuestionType getQuestionType();
}
