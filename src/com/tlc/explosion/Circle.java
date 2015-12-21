package com.tlc.explosion;

import java.util.Random;

public class Circle {
	
	public final static int diameter = 8;//直径
	private static Random mRandom = new Random();
	
	public float centerX;
	public float centerY;
	public float radius;
	public int color;
	public float alpha;

	
	public void update(float pAnimValue, int pViewW, int pViewH){
		centerX = centerX + pAnimValue * mRandom.nextInt(pViewW) * (mRandom.nextFloat() - 0.5f);
		centerY = centerY + pAnimValue * mRandom.nextInt(pViewH / 2);

        radius = radius - pAnimValue * mRandom.nextInt(2);

        alpha = (1f - pAnimValue) * (1 + mRandom.nextFloat());
	}
}
