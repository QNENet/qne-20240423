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

package com.qnenet.qne.objects.classes;

public class QPayload {

	public static final byte PLAIN_BYTES = 0;
	public static final byte ENCRYPTED_BYTES = 1;
	public static final byte PLAIN_STRING = 2;
	public static final byte ENCRYPTED_STRING = 3;
	public static final byte PLAIN_JSON = 4;
	public static final byte ENCRYPTED_JSON = 5;
	public static final byte PLAIN_OBJECT = 6;
	public static final byte ENCRYPTED_OBJECT = 7;
	
	public byte type;
	public byte[] bytes;


}
