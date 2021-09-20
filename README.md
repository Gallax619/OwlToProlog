# OwlToProlog
Un programma Java che legge un'ontologia OWL2 e la traduce in un programma prolog.
Il programma funziona da riga di comando.
Parametri in input:
  1. path dell'ontologia da tradurre
  2. [opzionale] path della directory in cui salvare l'output. Se non specificato verrà salvato in ./output

L'output è composto da 3 file: 
  1. il programma prolog
  2. un file contente gli assiomi originali da cui l'ontologia deriva, ordinati per indice
  3. un file contenente le classi e le proprietà OWL del programma prolog, ordinate per indice.
