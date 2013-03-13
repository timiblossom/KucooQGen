package me.kucoo.graph;

public class QuestionEntity {
	public QuestionType type;
	public String question;
	public String[] possibleAnwers;
	public String answer;
	public String mid;
	public String property;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type + "\n");
		sb.append(question + "\n");
		for(int i=0; i<possibleAnwers.length; i++)
			sb.append("\t" + String.valueOf(i+1) + " " + possibleAnwers[i] + "\n");
		sb.append("Answer : " + answer + "\n");
		sb.append("\tMid : " + mid);
		sb.append("\tProperty : " + property);
		return sb.toString();
		
	}
}
