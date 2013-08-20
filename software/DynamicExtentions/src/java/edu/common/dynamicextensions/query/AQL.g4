grammar AQL;

query : SELECT select_list WHERE expr #QueryExpr
      ;
      
select_list : FIELD (',' FIELD)* #SelectList
	 ;

expr : expr AND expr       #AndExpr
     | expr OR expr        #OrExpr
     | LP expr RP          #ParensExpr
     | NOT expr            #NotExpr
     | cond                #CondExpr
     ;
     
cond : FIELD OP SLITERAL
     | FIELD OP INT
     | FIELD OP FLOAT
     ;
     

WS: [ \t\n\r]+ -> skip;

SELECT: 'select';
WHERE : 'where';
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
OP: ('>'|'<'|'>='|'<='|'='|'!='|'like');

fragment
SGUTS: (ESC | ~('\\' | '"'))*;
QUOTE: '"';
