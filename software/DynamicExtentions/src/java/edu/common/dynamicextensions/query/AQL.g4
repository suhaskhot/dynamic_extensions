grammar AQL;

query : SELECT select_list WHERE expr #QueryExpr
      ;
      
select_list : arith_expr (',' arith_expr)* #SelectList
	 ;

expr : expr AND expr       #AndExpr
     | expr OR expr        #OrExpr
     | LP expr RP          #ParensExpr
     | NOT expr            #NotExpr
     | cond                #CondExpr
     ;
     
cond : arith_expr OP arith_expr
	 ;
	 
arith_expr : arith_expr ARITH_OP arith_expr   #ArithExpr
		  | arith_expr ARITH_OP date_interval #DateIntervalExpr
		  | MONTHS LP arith_expr RP           #MonthsDiff
		  | YEARS LP arith_expr RP            #YearsDiff  
		  | LP arith_expr RP 		          #ParensArithExpr
          | FIELD						      #FieldExpr
          | SLITERAL					      #StringExpr
          | INT							      #IntExpr
          | FLOAT						      #FloatExpr
          ;	 
          
date_interval : YEAR MONTH? DAY?
              | YEAR? MONTH DAY?
              | YEAR? MONTH? DAY
              ;          
                   
               
WS: [ \t\n\r]+ -> skip;

SELECT: 'select';
WHERE : 'where';
MONTHS: 'months';
YEARS: 'years';
OR: 'or';
AND: 'and';
NOT: 'not';
LP: '(';
RP: ')';

FIELD: (INT|ID) '.' ID ('.' ID)*;
INT: '-'? DIGIT+;
FLOAT: '-'? DIGIT+ '.' DIGIT+;
YEAR: DIGIT+ ('y'|'Y');
MONTH: DIGIT+ ('m'|'M');
DAY: DIGIT+ ('d'|'D');
DIGIT: ('0'..'9');
SLITERAL: '"' SGUTS '"';
ESC: '\\' ('\\' | '"');
ID: ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
OP: ('>'|'<'|'>='|'<='|'='|'!='|'like');
ARITH_OP: ('+'|'-'|'*'|'/');

fragment
SGUTS: (ESC | ~('\\' | '"'))*;
QUOTE: '"';
