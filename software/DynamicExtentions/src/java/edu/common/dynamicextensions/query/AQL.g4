grammar AQL;

query : expr
      ;

expr : expr AND expr       #AndExpr
     | expr OR expr        #OrExpr
     | LP expr RP          #ParensExpr
     | NOT expr            #NotExpr
     | cond                #CondExpr
     ;
     
cond : FIELD SOP SLITERAL
     | FIELD NOP INT
     | FIELD NOP FLOAT
     ;
     

WS: [ \t\n\r]+ -> skip;

OR: 'or';
AND: 'and';
NOT: 'not';
LP: '(';
RP: ')';

FIELD: (INT|ID) '.' ID ('.' ID)*;
INT: '-'? ('0'..'9')+;
FLOAT: '-'? ('0'..'9')+ '.' ('0'..'9')+;
SLITERAL: '"' SGUTS '"';
ESC: '\\' ('\\' | '"');
ID: ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
NOP: ('>'|'<'|'>='|'<='|'='|'!=');
SOP: ('='|'!='|'like');

fragment
SGUTS: (ESC | ~('\\' | '"'))*;
QUOTE: '"';
