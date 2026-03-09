package com.axelpalacios.cardgame24;

import java.util.Stack;

class ExpressionParser {
    private String s;
    private int pos = -1, ch;

    public ExpressionParser(String s) {
        this.s = s;
    }

    public double parse() throws Exception {
        nextChar();
        double x = parseExpression();
        if (pos < s.length()) throw new Exception("Unexpected: " + (char)ch);
        return x;
    }

    private void nextChar() {
        pos++;
        ch = pos < s.length() ? s.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private double parseExpression() throws Exception {
        double x = parseTerm();
        for (;;) {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() throws Exception {
        double x = parseFactor();
        for (;;) {
            if (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else return x;
        }
    }

    private double parseFactor() throws Exception {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();

        double x;
        int startPos = this.pos;
        if (eat('(')) {
            x = parseExpression();
            if (!eat(')')) throw new Exception("Missing ')'");
        } else if ((ch >= '0' && ch <= '9')) {
            while ((ch >= '0' && ch <= '9')) nextChar();
            x = Integer.parseInt(s.substring(startPos, this.pos));
        } else {
            throw new Exception("Unexpected: " + (char)ch);
        }

        return x;
    }
}