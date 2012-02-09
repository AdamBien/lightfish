/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightfish.business.logging;

/**
 * User: blog.adam-bien.com
 * Date: 09.02.12
 * Time: 05:27
 */
public class Log {
    public void error(String msg,Exception e){
        System.err.println(msg + " Exception: " +e);
    }

    public void info(String msg) {
        System.out.println(msg);
    }
}
