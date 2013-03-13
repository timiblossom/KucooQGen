namespace java me.kucoo.graph.thrift

struct Fact {
 1: string mid,
 2: string property
}


struct Film {
  1: string title,
  2: string imageUrl,
  3: string mid
}


struct Question { 
 1: string question, 
 2: list<string> answers, 
 3: i16 correctAnswer, 
 4: list<Fact> facts
}


service QGen {
  list<Film> getPopularFilms(1: string level),
  list<Question> getQuestions(1: i16 n, 2: list<Film> films, 3: set<string> excludedSet)
  list<Question> getRandomQuestions(1: i16 n, 2: list<Film> films)
}


exception InvalidOperation {
  1: i16 errorId,
  2: string reason
}
