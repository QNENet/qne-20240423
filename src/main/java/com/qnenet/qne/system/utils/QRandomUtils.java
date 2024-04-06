/*
 * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qnenet.qne.system.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//import io.netty.util.internal.ThreadLocalRandom;

public class QRandomUtils {

	private static Random random = new Random();

	
	public static int randomInt() {
		return random.nextInt();
	}

	public static long randomLong() {
		return random.nextLong();
	}
	
	public static short randomShort() {
		return (short) randomIntBetween(Short.MIN_VALUE, Short.MAX_VALUE);
	}

	
	public static int randomIntBetween(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static long randomLongBetween(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max + 1);
	}

	public static String generateRandomName(int length) {
		String characters = "abcdefghijklmnopqrstuvwxyz";
		char[] options = characters.toCharArray();
		char[] result = new char[length];
		Random r = new Random();
		for (int i = 0; i < result.length; i++) {
			result[i] = options[r.nextInt(options.length)];
		}
		return new String(result);
	}


    public static byte[] randomBytes(int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = (byte) randomIntBetween(0, 9);
		}
		return result;
	}
}
