/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package common;

import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.api.Randomizer;
import io.github.benas.jpopulator.impl.PopulatorBuilder;
import io.github.benas.jpopulator.randomizers.CityRandomizer;
import io.github.benas.jpopulator.randomizers.CountryRandomizer;
import io.github.benas.jpopulator.randomizers.DateRangeRandomizer;
import io.github.benas.jpopulator.randomizers.EmailRandomizer;
import io.github.benas.jpopulator.randomizers.FirstNameRandomizer;
import io.github.benas.jpopulator.randomizers.GenericStringRandomizer;
import io.github.benas.jpopulator.randomizers.LastNameRandomizer;
import io.github.benas.jpopulator.randomizers.StreetRandomizer;

/**
 * Utility class used to generate random customers.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CustomerGenerator {

    public static Populator customerPopulator;

    static {
        customerPopulator = buildCustomerPopulator();
    }

    public static Customer generateCustomer() {
        return customerPopulator.populateBean(Customer.class);
    }

    public static void main(String[] args) throws Exception {
        int customersCount = Integer.parseInt(System.getProperty("org.easybatch.bench.count"));
        String customersFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "customers_in.csv";
        generateCsvCustomers(customersFile, customersCount);
        System.out.println("Successfully generated " + customersCount + " random customers in " + customersFile);
    }

    public static void generateCsvCustomers(String customersFile, int customersCount) throws Exception {
        FileWriter fileWriter = new FileWriter(customersFile);
        for (int i = 0; i < customersCount; i++) {
            Customer customer = CustomerGenerator.generateCustomer();
            fileWriter.write(CustomerGenerator.toCsv(customer) + "\n");
            fileWriter.flush();
        }
        fileWriter.close();
    }

    public static Populator buildCustomerPopulator() {
        Date today = new Date();
        Date nextYear = new Date();nextYear.setYear(today.getYear() + 1);
        return new PopulatorBuilder()
                .registerRandomizer(Customer.class, Integer.TYPE, "id", new Randomizer() {
                    Random random = new Random();
                    @Override
                    public Integer getRandomValue() {
                        return Math.abs(random.nextInt(1000000));
                    }
                })
                .registerRandomizer(Customer.class, String.class, "firstName", new FirstNameRandomizer())
                .registerRandomizer(Customer.class, String.class, "lastName", new LastNameRandomizer())
                .registerRandomizer(Customer.class, Date.class, "birthDate", new DateRangeRandomizer(today, nextYear))
                .registerRandomizer(Customer.class, String.class, "email", new EmailRandomizer())
                .registerRandomizer(Customer.class, String.class, "phone", new GenericStringRandomizer(
                        new String[]{"0102030405","0607080910","0504030201","0610090807"}))
                .registerRandomizer(Customer.class, String.class, "street", new StreetRandomizer())
                .registerRandomizer(Customer.class, String.class, "zipCode", new GenericStringRandomizer(
                        new String[]{"54321", "12345", "98765", "56789"}))
                .registerRandomizer(Customer.class, String.class, "city", new CityRandomizer())
                .registerRandomizer(Customer.class, String.class, "country", new CountryRandomizer())
                .build();
    }

    public static String toCsv(Customer customer) {
        return MessageFormat.format("{0},{1},{2},{3,date,yyyy-MM-dd},{4},{5},{6},{7},{8},{9}",
                String.valueOf(customer.getId()), customer.getFirstName(), customer.getLastName(), customer.getBirthDate(),
                customer.getEmail(), customer.getPhone(), customer.getStreet(), customer.getZipCode(), customer.getCity(), customer.getCountry());
    }

}
